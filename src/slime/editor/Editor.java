package slime.editor;

import slime.absynt.*;
import slime.layout.*;
import slime.utils.*;

import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * <BR> <BR>
 * Feel free to play around with this version of an SFC-editor.  
 * <br><br>
 * Status: complete. <br>
 * Known bugs: Some semantic problems. 
 * But who cares when there are no checks.<br>
 * @author Andreas Niemann
 * @version $Id: Editor.java,v 1.19 2002-07-09 23:37:24 swprakt Exp $
 */

// FIX ME: Double Buffering dringend notwendig !!!
// FIX ME: Selektierte Items muessen zuletzt gezeichnet werden !!!

public final class Editor extends JComponent implements ChangeListener {

    protected static final Color BACKGROUND_COLOR = Color.lightGray;
    protected static final Color HIGHLIGHT_COLOR  = Color.yellow;
    protected static final Color SELECTABLE_COLOR = Color.orange;

    private static final int EDIT_MODE   = 0; 
    private static final int SOURCE_MODE = 1; 
    private static final int TARGET_MODE = 2; 
    private static final int REMOVE_MODE = 3; 

    private boolean           simulatorActive       = false;
    private Object            selectedItem          = null;

    private int               mode                  = EDIT_MODE;
    
    private ESFCList          eSFCList              = new ESFCList();
    private ESFC              eSFC                  = null;
    private JTabbedPane       drawBoardTabbedPane   = new JTabbedPane();
    private static ScrollPane helpPane;
    private ScrollList        guardScrollList;
    private ScrollList        actionScrollList;
    private ScrollList        declarationScrollList;
    private MenuAndStatePanel menuAndStatePanel;

    /**
     * Creates an editor with an 'empty' SFC.
     */
    public Editor() {
	this(new SFC());
    }

    /**
     * Creates an editor with the given SFC.
     */
    public Editor(SFC sfc) {
	this.setBackground(BACKGROUND_COLOR);

	this.setupMenueAndStatePanel();
	this.setupScrollLists();

	this.drawBoardTabbedPane.addChangeListener(this);
	this.addHelpPane();
	this.add(sfc);
	this.initWindow("SFC-Editor");

    }

    private void setupMenueAndStatePanel() {
	this.menuAndStatePanel = new MenuAndStatePanel(this);
	this.menuAndStatePanel.setMode("E");
	this.menuAndStatePanel.setBorder(
	    this.getTitledBorder("Menu & Status"));
    }

    /*
     * Sets up the scroll lists for several sfc components.
     */
    private void setupScrollLists() {
	this.setupDeclarationScrollList();
	this.setupActionScrollList();
	this.setupGuardScrollList();
    }
    
    /*
     * Sets up a scroll list for the declarations of a sfc.
     */
    private void setupDeclarationScrollList() {
	this.declarationScrollList = new ScrollList(BACKGROUND_COLOR, 
						    "Add new declaration");
	this.declarationScrollList.setBorder(
	    this.getTitledBorder("Declarations"));
	this.declarationScrollList.getList().addMouseListener(
	    new DeclarationListListener(this));
	this.declarationScrollList.getButton().addActionListener(
	    new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    menuAndStatePanel.setStatusMessage(
			"Adding new declaration ...");
		    addDeclaration();
		}
	    });
    }

    /*
     * Sets up a scroll list for the actions of a sfc.
     */
    private void setupActionScrollList() {
	this.actionScrollList = new ScrollList(BACKGROUND_COLOR, 
					       "Add new action");
	this.actionScrollList.setBorder(this.getTitledBorder("Actions"));
	this.actionScrollList.getList().addMouseListener(
	    new ActionListListener(this));
	this.actionScrollList.getButton().addActionListener(
	    new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    menuAndStatePanel.setStatusMessage(
			"Adding new action ...");
		    addAction();
		}
	    });
    }
    
    /*
     * Sets up a scroll list for the guards of a sfc.
     */
    private void setupGuardScrollList() {
	this.guardScrollList = new ScrollList(BACKGROUND_COLOR);
	this.guardScrollList.setBorder(
	    this.getTitledBorder("Guards"));
    }

    public void stateChanged(ChangeEvent e) {
	int index = this.drawBoardTabbedPane.getSelectedIndex();
	if (index != 0) {
	    this.eSFC = eSFCList.get(index-1);
	    this.eSFC.getDrawBoard().repaint();
	} else {
	    this.eSFC = null;
	    helpPane.repaint();
	}
	if (menuAndStatePanel != null) 
	    this.menuAndStatePanel.enableButtons();

	this.updateWindow();
    }

    /**
     * Closes the actual selected sfc.
     */
    public void closeSelectedSFC() {
	this.eSFCList.remove(this.eSFC);
	int index = this.drawBoardTabbedPane.getSelectedIndex();
	this.drawBoardTabbedPane.removeTabAt(index);
	this.drawBoardTabbedPane.setSelectedIndex(index-1);
    } 

    /**
     * Adds the given sfc to this editor in a seperate DrawBoard.
     */
    public void add(SFC sfc) {
	this.eSFC = new ESFC(sfc);
	this.eSFC.setDrawBoard(new DrawBoard(this, this.eSFC));
	this.eSFCList.add(this.eSFC);
	this.addTabbedPane(this.eSFC);
    }

    /**
     * Adds a new sfc under the given name to this editor 
     * on a seperate DrawBoard.
     */
    public void add(String name) {
	if (name.equals(""))
	    this.add(new SFC());
	else
	    this.add(new SFC(), name+".sfc");
    }

    protected void add(SFC sfc, String name) {
	int i = name.lastIndexOf('.');
	name = name.substring(0,i);
	sfc.name = name;
	this.add(sfc);
    }

    
    /**
     * Return the current selected ESFC of this editor or null if no ESFC is selected.
     */
    protected ESFC getExtendedSelectedSFC() {
	return this.eSFC;
    }

    /**
     * Returns the actual SFC used by the editor or null if no SFC is selected.
     */
    public SFC getSelectedSFC() {
	if (this.eSFC != null)
	    return this.eSFC.getSFC();
	return null;
    }

    protected void setSimulatorActive(boolean active) {
	this.simulatorActive = active;
	this.menuAndStatePanel.enableButtons();
    }

    protected boolean getSimulatorActive() {
	return this.simulatorActive;
    }

    private void initWindow(String title) {
	this.setBackground(BACKGROUND_COLOR);
	this.setLayout(new BorderLayout());
	this.add(menuAndStatePanel, BorderLayout.SOUTH);
	this.add(this.getCenterPanel(), BorderLayout.CENTER);
	JPanel westPanel = new JPanel();
	westPanel.setLayout( new BorderLayout() );
	westPanel.add(this.guardScrollList,
		      BorderLayout.NORTH);
	westPanel.add(this.actionScrollList,
		      BorderLayout.CENTER);
	westPanel.add(this.declarationScrollList, 
		      BorderLayout.SOUTH);
	this.add(westPanel, BorderLayout.WEST);
	this.updateWindow();
	this.setBorder(this.getTitledBorder("Editor"));
	this.setVisible(true);
    }

    /**
     * Updates the editors components.
     */
    public void updateWindow() {
	if (this.eSFC != null) {
	    SFC sfc = this.eSFC.getSFC();
	    this.guardScrollList.setList(sfc.transs);
	    this.actionScrollList.setList(sfc.actions);
	    this.declarationScrollList.setList(sfc.declist);
	} else {
	    this.guardScrollList.removeList();
	    this.actionScrollList.removeList();
	    this.declarationScrollList.removeList();
	}
	if ((this.eSFC == null) || this.getSimulatorActive()) {
	    this.guardScrollList.unuseable();
	    this.actionScrollList.unuseable();
	    this.declarationScrollList.unuseable();
	}	
    }

    /**
     * Returns a titled border with the given name.
     */
    protected TitledBorder getTitledBorder(String title) {
	TitledBorder titledBorder = new TitledBorder(title);
	titledBorder.setTitleFont(new Font("Courier", Font.PLAIN, 14));
	titledBorder.setTitleColor(Color.blue);
	return titledBorder;
    }

    private JPanel getCenterPanel() {
	JPanel panel = new JPanel();
	panel.add(this.drawBoardTabbedPane);
	panel.setBackground(BACKGROUND_COLOR);
	panel.setBorder(this.getTitledBorder("SFC's"));
	return panel;
    }

    private void addTabbedPane(ESFC eSFC) {
	ScrollPane jsp = new ScrollPane();
	jsp.setSize(700,480);
	jsp.add(eSFC.getDrawBoard());
	this.drawBoardTabbedPane.add(eSFC.getSFC().name, jsp);
	this.drawBoardTabbedPane.setSelectedComponent(jsp);
    }

    private void  addHelpPane() {
	helpPane = new ScrollPane();
	helpPane.setSize(640,480);
	JTextArea tA = new JTextArea(20,40);
	tA.append("\n*** STEPS ***\n\n");
	tA.append("- Add a new step ...\n"
		  +"Leftclick on a free place on the drawboard. You may mark the step as initial and bind some actions from the action list to it. Marking a step as initial sets the old initial step to normal step.\n\n");
	tA.append("- Delete a step ...\n"
		  +"Switch with 'r' to REMOVE-mode and leftclick on the step. All transitions which are connected with this step will also be deleted.\n\n");
	tA.append("- Change the name of a step ...\n"
		  +"Switch with 'e' to EDIT-mode and click on the step.\n\n");
	tA.append("- Mark a step as initial ...\n"
		  +"Switch with 'e' to EDIT-mode, click on the step and check the 'Initial step' toggle button.\n\n");
	tA.append("- Mark a step as source ...\n"
		  +"Switch with 's' to SOURCE-mode and leftclick on the step.\n\n");
	tA.append("- Mark a step as target ...\n"
		  +"Switch with 't' to TARGET-mode and leftclick on the step.\n\n");
	tA.append("- Unmark all steps ...\n"
		  +"Press 'c' to unmark all source and target steps.\n\n");
	tA.append("- Move a step ...\n"
		  +"Switch with 'e' to EDIT-mode and drag the step with the left mousebutton hold down.\n\n");
	tA.append("- Move a group of steps ...\n"
		  +"Mark the set of steps you wish to move as a set of source or target steps and move them like one step.\n\n");

	tA.append("\n*** TRANSITIONS ***\n\n");
	tA.append("- Add transition(s) between marked source and target steps ...\n"
		  +"Press 'g' and select or input a guard.\n\n");
	tA.append("- Change a guard at a transition ...\n"
		  +"Switch with 'e' to EDIT-mode and click on the guard.\n\n");


	tA.append("\n*** ACTIONS ***\n\n");
	tA.append("- Modify an action in the action list ...\n"
		  +"Double click on the action in the list.\n\n");


	tA.setEnabled(false);
	tA.setWrapStyleWord(true);
	tA.setLineWrap(true);
	tA.setFont(new Font("Courier", Font.PLAIN, 14));
	helpPane.add(tA);
	this.drawBoardTabbedPane.add("Help", helpPane);
	this.drawBoardTabbedPane.setSelectedComponent(helpPane);
    }
    
    private void stepDialogWindow(String title, Step step, int x, int y) {

	ScrollList actionScrollList = new ScrollList(BACKGROUND_COLOR, 
						     this.eSFC.getSFC().actions);
	int[] selectedIndices = this.getActionIndices(step);
	actionScrollList.setSelectedIndices(selectedIndices);

	boolean isIStep = (step == this.eSFC.getSFC().istep);
	Object[] message = new Object[5];
	String name = "";
	if (step != null)
	    name = step.name;
	message[0] = "Name:";
	message[1] = (new JTextField(name));
	message[2] = "Choose actions:";
	message[3] = (actionScrollList.getScrollList());
	message[4] = (new JRadioButton("Initial step", isIStep));
	String[] options = {"Ok", "Cancel"};
	
	int result = JOptionPane.showOptionDialog(
	    null,
	    message,
	    title,
	    JOptionPane.DEFAULT_OPTION,
	    JOptionPane.PLAIN_MESSAGE,
	    null,
	    options,
	    options[0]);
	if (result == 0) {
	    this.eSFC.setChecked(false);
	    this.menuAndStatePanel.enableButtons();
	    if (step == null)
		step = this.eSFC.addStep(((JTextField)message[1]).getText(), x, y);
	    step.name = ((JTextField)message[1]).getText();
	    if (((JRadioButton)message[4]).isSelected())
		this.eSFC.getSFC().istep = step;
	    else
		if (isIStep)
		    this.eSFC.getSFC().istep = null;
	    step.actions = this.getStepActions(actionScrollList.getList());
	}
    }
    
    // FIX ME: Auf jeden Fall optimieren !!!
    private int[] getActionIndices(Step step) {
	SFC sfc = this.eSFC.getSFC();
	if (sfc == null)
	    return new int[0];
	LinkedList actionList = sfc.actions;
	LinkedList stepActionList = new LinkedList();
	if (step != null)
	    stepActionList = step.actions;
	int[] actionIndizies = new int[stepActionList.size()];
	for (int i=0; i<stepActionList.size(); i++) {
	    StepAction stepAction = (StepAction)stepActionList.get(i);
	    for (int j=0; j<actionList.size(); j++) {
		slime.absynt.Action action = (slime.absynt.Action)actionList.get(j);
		if (action.a_name.equals(stepAction.a_name)) 
		    actionIndizies[i] = j;
	    }
	}
	return actionIndizies;
    }

    private LinkedList getStepActions(JList actionJList) {
	LinkedList stepAction = new LinkedList();
	int[] selectedIndices = actionJList.getSelectedIndices();
	LinkedList actionList = this.eSFC.getSFC().actions;
	for (int i=0; i<selectedIndices.length; i++) {
	    slime.absynt.Action action = (slime.absynt.Action)actionList.get(selectedIndices[i]);
	    stepAction.add(new StepAction(new Nqual(), action.a_name));
	}
	return stepAction;
    }

    protected void evaluateSingleMouseClickOn(int x, int y) {
	Object object = this.determineSelectedItem(x, y);
	if (getSimulatorActive()) {
	    if (object instanceof StepAction)
		object = (Object)this.eSFC.getAction(((StepAction)object).a_name);
	    this.selectedItem = object;
	    return;
	}
	if (object != null) {
	    if (object instanceof Step) {
		Step step = (Step)object;
		if (mode == REMOVE_MODE) {
		    this.eSFC.removeStep(step);
		    this.menuAndStatePanel.setStatusMessage("Step "
							    +step.name
							    +" removed.");
		    this.menuAndStatePanel.enableButtons();
		}
		else if (mode == SOURCE_MODE) 
		    this.eSFC.addSourceStep(step);
		else if (mode == TARGET_MODE)
		    this.eSFC.addTargetStep(step);
		else if (mode == EDIT_MODE) {
		    this.menuAndStatePanel.setStatusMessage("Change step.");
		    this.stepDialogWindow("Change step ...", step, x, y);
		}
	    } else if (object instanceof Transition) {
		Transition transition = (Transition)object;
		if (mode == REMOVE_MODE) {
		    this.eSFC.removeTransition(transition);
		} 
		else if (mode == EDIT_MODE) {
		    this.menuAndStatePanel.setStatusMessage("Change guard.");
		    Expr guard = this.chooseAGuardDialog(ESFC.output(transition.guard));
		    if (guard != null) {
			transition.guard = guard;
			this.menuAndStatePanel.setStatusMessage("Guard changed.");
		    } else
			this.menuAndStatePanel.setStatusMessage("Guard not changed.");
		}
	    }
	}
	else {
	    this.menuAndStatePanel.setStatusMessage("Adding a new step.");
	    this.stepDialogWindow("New step ...", null, x, y);
	}
	this.updateWindow();
    }
    
    protected void evaluateMouseDragged(int x0, int y0, int x1, int y1) {
	if (this.getSimulatorActive())
	    return;
	if (mode == EDIT_MODE) {
	    Step step = this.determineSelectedStep(x0, y0);
	    if (step != null) {
		LinkedList stepList = new LinkedList();
		stepList.add(step);
		if (this.eSFC.isSourceStep(step))
		    stepList = this.eSFC.getSourceSteps();
		if (this.eSFC.isTargetStep(step))
		    stepList = this.eSFC.getTargetSteps();
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		SFC sfc = this.eSFC.getSFC();
		for (int i=0; i<stepList.size(); i++) {
		    Position position = ((Step)stepList.get(i)).pos;
		    int newX = (int)position.x + deltaX;
		    int newY = (int)position.y + deltaY;
		    position.x = (float)newX;
		    position.y = (float)newY;
		}
		this.eSFC.setLayouted(false);
		this.menuAndStatePanel.enableButtons();
	    }
  	}    
    }

    protected boolean evaluateMouseMoved(int x, int y) {
	Object object = this.determineSelectedItem(x, y);
	Object o = this.eSFC.getSelectedObject();
	this.eSFC.setSelectedObject(object);
	if (object != o) {
	    if (object instanceof Step) {
		int[] selectedIndices = this.getActionIndices((Step)object);
		this.actionScrollList.setSelectedIndices(selectedIndices);
	    }
	}
	return (object != o);
    }

    private Step determineSelectedStep(int x, int y) {
	Object object = this.determineSelectedItem(x, y);
	if (object instanceof Step)
	    return (Step)object;
	return null;
    }

    private Object determineSelectedItem(int x, int y) {

	LinkedList stepList = this.eSFC.getSFC().steps;
	for (int i=0; i<stepList.size(); i++) {
	    Step step = (Step)stepList.get(i);
	    if (this.isInStep(step, x, y))
		return step;
	    StepAction stepAction = isInAction(step, x, y);
	    if (stepAction != null)
		return stepAction;
	}

	Hashtable guardPositions = this.eSFC.getGuardPositions();
	LinkedList transitions = this.eSFC.getSFC().transs;
	for (int i=0; i<transitions.size(); i++) {
	    Transition transition = (Transition)transitions.get(i);
	    if (guardPositions.containsKey(transition)) {
		Position position = (Position)guardPositions.get(transition);
		int x2 = (int)position.x;
		int y2 = (int)position.y;
		if (((x+2) >= x2) &&
		    ((y+3) >= y2) &&
		    ((x-11) <= x2) &&
		    ((y-3) <= y2))
		    return transition;
	    }
	}

	return null;
    }
    
    private boolean isInStep(Step step, int x, int y) {
	Position position = step.pos;
	// FIX-ME: Checker?
	if (position == null) {
	    position = new Position(0.0f, 0.0f);
	    step.pos = position;
	}
	int xOfStep   = (int)position.x;
	int yOfStep   = (int)position.y;
	int stepWidth = this.eSFC.getWidth(step);

	if (x < xOfStep)               return false;
	if (y < yOfStep)               return false;
	if (x > (xOfStep + stepWidth)) return false;
	if (y > (yOfStep + 30))        return false;
	return true;
    }

    private StepAction isInAction(Step step, int x, int y) {
	Position position = step.pos;
	// FIX-ME: Checker?
	if (position == null) {
	    position = new Position(0.0f, 0.0f);
	    step.pos = position;
	}
	int xOfStep     = (int)position.x;
	int yOfStep     = (int)position.y;
	int stepWidth   = this.eSFC.getWidth(step);
	int stepActions = step.actions.size();
	xOfStep += (stepWidth + SFCPainter.ACTION_GAP);
	stepWidth = 13;
	if (x < xOfStep)                    return null;
	if (y < yOfStep)                    return null;
	if (x > (xOfStep + stepWidth))      return null;
	if (y >= (yOfStep + stepActions*15)) return null;
	int number = (y - yOfStep) / 15;
	return (StepAction)(step.actions).get(number);
    }

    protected int toggleMode(int newMode) {
	int oldMode = this.mode;

	this.mode   = newMode;
	return oldMode;
    }

    protected int toggleMode() {
	int newMode = (this.mode + 1) % 4;

	this.toggleMode(newMode);
	return newMode;
    }

    protected void evaluateKeyTyped(char c) {
	if (c == (new String("e")).charAt(0)) {
	    this.menuAndStatePanel.setMode("E");
	    this.menuAndStatePanel.setStatusMessage("Toggled to EDIT-mode");
	    this.toggleMode(EDIT_MODE);
	}
	if (c == (new String("s")).charAt(0)) {
	    this.menuAndStatePanel.setMode("S");
	    this.menuAndStatePanel.setStatusMessage("Toggled to SOURCE-mode");
	    this.toggleMode(SOURCE_MODE);
	}
	if (c == (new String("t")).charAt(0)) {
	    this.menuAndStatePanel.setMode("T");
	    this.menuAndStatePanel.setStatusMessage("Toggled to TARGET-mode");
	    this.toggleMode(TARGET_MODE);
	}
	if (c == (new String("r")).charAt(0)) {
	    this.menuAndStatePanel.setMode("R");
	    this.menuAndStatePanel.setStatusMessage("Toggled to REMOVE-mode");
	    this.toggleMode(REMOVE_MODE);
	}
	if (c == (new String("c")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Unmarked all steps");
	    this.eSFC.clearSourceSteps();
	    this.eSFC.clearTargetSteps();
	}
	if (c == (new String("g")).charAt(0)) {
	    if (this.eSFC.getNumberOfSourceSteps() == 0) {
		this.menuAndStatePanel.setStatusMessage("No source step(s) selected");
		return;
	    }
	    if (this.eSFC.getNumberOfTargetSteps() == 0) {
		this.menuAndStatePanel.setStatusMessage("No target step(s) selected");
		return;
	    }
	    Expr guard = this.chooseAGuardDialog("");
	    if (guard != null) {
		this.menuAndStatePanel.setStatusMessage("New transition added");
		this.eSFC.addTransition(this.eSFC.getSourceSteps(), 
					guard, 
					this.eSFC.getTargetSteps());
		this.eSFC.clearSourceSteps();
		this.eSFC.clearTargetSteps();
		this.toggleMode(SOURCE_MODE);
		this.updateWindow();
	    } 
	}
    }

    private Expr chooseAGuardDialog(String oldExpr) { 
	ScrollList guardScrollList = new ScrollList(BACKGROUND_COLOR,
						    this.eSFC.getSFC().transs);
	guardScrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	Object[] message = new Object[4];
	message[0] = "Expression";
	message[1] = (new JTextField(oldExpr));
	message[2] = "Existing guards";
	message[3] = (guardScrollList.getScrollList());
	String[] options = {"Ok", "Cancel"};
	
	int result = JOptionPane.showOptionDialog(
	    null,
	    message,
	    "Choose a guard",
	    JOptionPane.DEFAULT_OPTION,
	    JOptionPane.PLAIN_MESSAGE,
	    null,
	    options,
	    options[0]);
	if (result == 0) {
	    this.eSFC.setChecked(false);
	    this.menuAndStatePanel.enableButtons();
	    if (((JTextField)message[1]).getText().equals("")) {
		JList list = guardScrollList.getList();
		    String name = (String)list.getSelectedValue();
		    if (name == null) {
			this.menuAndStatePanel.setStatusMessage("No guard selected");
		    } else {
			Expr eGuard = null; 
			LinkedList transitionList = this.eSFC.getSFC().transs;
			for (int i=0; i<transitionList.size(); i++) {
			    Transition transition = (Transition)transitionList.get(i);
			    Expr guard = transition.guard;
			    if (this.eSFC.output(guard).equals(name))
				eGuard = guard;
			}
			return eGuard;
		    }
	    } else {
		
		String exprString = ((JTextField)message[1]).getText();
		Expr expr = null;
		
		slime.utils.ExprParser parser = new slime.utils.ExprParser();
		try {
		    expr = parser.parseExpr(exprString);
		} catch (ParseException e) {
		    this.menuAndStatePanel.setStatusMessage("Parser fails on Expr");
		} catch (Exception e) {
		    this.menuAndStatePanel.setStatusMessage("Oh no !!!!");
		}
		return expr;
	    }
	}
	return null;
    }
    
    protected void reactOnChangeOfAction() {
	if (this.getSimulatorActive()) return;

	int[] indices = this.actionScrollList.getSelectedIndices();

	if (indices.length == 0)
	    return;
	
	LinkedList actions = this.eSFC.getSFC().actions;
	slime.absynt.Action action = 
	    (slime.absynt.Action)actions.get(indices[0]);
	this.changeAction(action);
    }
    
    private void changeAction(slime.absynt.Action action) {
	String name       = action.a_name;
	String sapString  = ESFC.output(action);
	int index = sapString.indexOf(' ');
	sapString = sapString.substring(index+1);
	slime.absynt.Action newAction =
	    dialogForAction(name, sapString, "Change action");
	if (newAction != null) {
	    // Folgendes if notwendig? Kann ja immer gemacht werden ...
	    if (!name.equals(newAction.a_name)) {
		if (!this.eSFC.actionNameExists(newAction.a_name)) {
		    action.a_name = newAction.a_name;
		    LinkedList steps = this.eSFC.getSFC().steps;
		    for (int i=0; i<steps.size(); i++) {
			Step step = (Step)steps.get(i);
			LinkedList stepActions = step.actions;
			for (int j=0; j<stepActions.size(); j++) {
			    StepAction stepAction = (StepAction)stepActions.get(j);
			    if (name.equals(stepAction.a_name))
				stepAction.a_name = newAction.a_name;
			}
		    }
		} else {
		    this.menuAndStatePanel.setStatusMessage("New name of action already exists, try again :-)");
		}
	    }
	    action.sap = newAction.sap;
	    this.menuAndStatePanel.setStatusMessage("Action changed.");
	    this.updateWindow();
	} else {
	    this.menuAndStatePanel.setStatusMessage("Action has not changed.");
	}	    
    }

    private void addAction() {
	slime.absynt.Action newAction =
	    dialogForAction("", "", "Add new action");
	if (newAction != null) { 
	    if (!this.eSFC.actionNameExists(newAction.a_name)) {
  		this.eSFC.addAction(newAction);
		this.menuAndStatePanel.setStatusMessage("New action added.");
		this.updateWindow();
	    } else {
		this.menuAndStatePanel.setStatusMessage("Name of action already exists. Modify action by double clicking on it in this list.");
	    }
	} else {
	    this.menuAndStatePanel.setStatusMessage("No action added.");
	}
    }

    protected void reactOnChangeOfDeclaration() {
	if (this.getSimulatorActive()) return;

	int[] indices = this.declarationScrollList.getSelectedIndices();

	if (indices.length == 0)
	    return;
	
	LinkedList declist = this.eSFC.getSFC().declist;
	Declaration declaration = 
	    (Declaration)declist.get(indices[0]);
	this.changeDeclaration(declaration);
    }

    private void changeDeclaration(Declaration declaration) {
	String variable = declaration.var.name;
	String value    = ESFC.output(declaration.val);
	String io       = new String();
	if (declaration.var.inputvar) io += "in";
	if (declaration.var.outputvar) io += "out";
	String type     = ESFC.output(declaration.type);
	Declaration newDeclaration =
	    dialogForDeclaration(variable, value, io, type, "Change declaration");
	if (newDeclaration != null) {
	    if (!(declaration.var.name).equals(newDeclaration.var.name))
		declaration.var  = newDeclaration.var;
	    declaration.var.inputvar = newDeclaration.var.inputvar; 
	    declaration.var.outputvar = newDeclaration.var.outputvar; 
	    declaration.type = newDeclaration.type;
	    declaration.val  = newDeclaration.val;
	    this.updateWindow();
	} else {
	    this.menuAndStatePanel.setStatusMessage("Declaration has not changed.");
	}	    
    }

    private void addDeclaration() {
	slime.absynt.Declaration newDeclaration =
	    dialogForDeclaration("", "", "", "[bool]", "Add new declaration");
	if (newDeclaration != null) { 
	    this.eSFC.getSFC().declist.add(newDeclaration);
	    this.menuAndStatePanel.setStatusMessage("New declaration added.");
	    this.updateWindow();
	} else {
	    this.menuAndStatePanel.setStatusMessage("No declaration added.");
	}
    }

    private slime.absynt.Action dialogForAction(String name, 
						String sapString, 
						String title) {
	Object[] message = new Object[4];
	message[0] = "Name:";
	message[1] = (new JTextField(name));
	message[2] = "SAP:";
	message[3] = (new JTextField(sapString));
	String[] options = {"Ok", "Cancel"};
	
	int result = JOptionPane.showOptionDialog(
	    null,
	    message,
	    title,
	    JOptionPane.DEFAULT_OPTION,
	    JOptionPane.PLAIN_MESSAGE,
	    null,
	    options,
	    options[0]);

	if (result == 0) {
	    this.eSFC.setChecked(false);
	    this.menuAndStatePanel.enableButtons();
	    sapString = ((JTextField)message[3]).getText();
  	    if (!sapString.equals("")) {
  		slime.utils.Parser parser = new slime.utils.Parser();
  		try {
  		    LinkedList sap = parser.parseSAP(sapString);
		    slime.absynt.Action action = 
			new slime.absynt.Action(((JTextField)message[1]).getText(), sap);
		    return action;
		} catch (ParseException e) {
  		    this.menuAndStatePanel.setStatusMessage("Parser fails on SAP");
  		} catch (Exception e) {
  		    this.menuAndStatePanel.setStatusMessage("Oh no !!!!");
  		}
  	    }
	}
	return null;
    }

    private Declaration dialogForDeclaration(String variable, 
					     String constant,
					     String io,
					     String type,
					     String title) {
	ButtonGroup  ioGroup = new ButtonGroup();
	JRadioButton none    = new JRadioButton("none");
	JRadioButton in      = new JRadioButton("IN");
	JRadioButton out     = new JRadioButton("OUT");
	JRadioButton inout   = new JRadioButton("INOUT");
	if (io.equals(""))      none.setSelected(true);
	if (io.equals("in"))    in.setSelected(true);
	if (io.equals("out"))   out.setSelected(true);
	if (io.equals("inout")) inout.setSelected(true);
	none.setActionCommand( "" );
	in.setActionCommand( "in" );
	out.setActionCommand( "out" );
	inout.setActionCommand( "inout" );
	ioGroup.add(none); 
	ioGroup.add(in);
	ioGroup.add(out);
	ioGroup.add(inout);

	ButtonGroup  typeGroup = new ButtonGroup();
	JRadioButton boolType  = new JRadioButton("BOOL");
	JRadioButton intType   = new JRadioButton("INT");
	if (type.equals("[bool]")) boolType.setSelected(true);
	if (type.equals("[int]"))  intType.setSelected(true);
	boolType.setActionCommand( "bool " );
	intType.setActionCommand( "int " );
	typeGroup.add(boolType);
	typeGroup.add(intType);

	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());

	JPanel typePanel = new JPanel();
	typePanel.add(boolType);
	typePanel.add(intType);
	panel.add(typePanel, BorderLayout.CENTER);

	JPanel ioPanel = new JPanel();
	ioPanel.add(none);
	ioPanel.add(in);
	ioPanel.add(out);
	ioPanel.add(inout);
	panel.add(ioPanel, BorderLayout.SOUTH);

	Object[] message = new Object[5];
	message[0] = "Name:";
	message[1] = (new JTextField(variable));
	message[2] = "Value:";
	message[3] = (new JTextField(constant));
	message[4] = panel;
	String[] options = {"Ok", "Cancel"};
	
	int result = JOptionPane.showOptionDialog(
	    null,
	    message,
	    title,
	    JOptionPane.DEFAULT_OPTION,
	    JOptionPane.PLAIN_MESSAGE,
	    null,
	    options,
	    options[0]);

  	if (result == 0) {
  	    this.eSFC.setChecked(false);
  	    this.menuAndStatePanel.enableButtons();
  	    String decString = new String();
	    decString += typeGroup.getSelection().getActionCommand()+" ";
	    decString += ioGroup.getSelection().getActionCommand()+" ";
	    decString += ((JTextField)message[1]).getText();
	    decString += " := ";
	    decString += ((JTextField)message[3]).getText();
	    slime.utils.DeclParser parser = new slime.utils.DeclParser();
	    try {
		slime.absynt.Declaration declaration = 
		    parser.parseDecl(decString);
		return declaration;
	    } catch (ParseException e) {
		this.menuAndStatePanel.setStatusMessage("Parser fails on declaration.");
	    } catch (Exception e) {
		this.menuAndStatePanel.setStatusMessage("Oh no !!!!");
	    }
  	}
	return null;
    }

    /**
     * Highlights the given object iff it is part of a SFC. Actually the
     * object may be a step or a transition.
     */
    public void highlight(Object object) {
	this.eSFC.highlight(object, HIGHLIGHT_COLOR);
    }

    /**
     * Removes highlight effect from the given object.
     */
    public void deHighlight(Object object) {
	this.eSFC.deHighlight(object);
    }

    public slime.absynt.Absynt getItemFromList(LinkedList items,
					       String     message) {
	this.getColors(items);
	this.menuAndStatePanel.setStatusMessage(message);
	this.selectedItem = null;
	this.stateChanged(null);
	while (!items.contains(this.selectedItem)) {}
	this.setColors(items);
	this.stateChanged(null);
	return (slime.absynt.Absynt)this.selectedItem;
    }

    private void getColors(LinkedList objects) {
	Hashtable colors = this.eSFC.getColorHashtable();

	for (int i=0; i<objects.size(); i++) 
	    colors.put(objects.get(i), SELECTABLE_COLOR);
    }
    
    private void setColors(LinkedList objects) {
	for (int i=0; i<objects.size(); i++) 
	    this.eSFC.deHighlight(objects.get(i));
    }
}



















