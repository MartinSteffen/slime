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
 * Feel free to play around with this initial version of an SFC-editor.  
 * @author Andreas Niemann
 * @version $Id: Editor.java,v 1.12 2002-06-12 19:52:58 swprakt Exp $
 */

public final class Editor extends JComponent 
    implements ChangeListener, MouseListener {

    private static final int WIDTH = 100, HEIGHT = 300;

    private static final int EDIT_MODE   = 0; // e
    private static final int SOURCE_MODE = 1; // s
    private static final int TARGET_MODE = 2; // t
    private static final int REMOVE_MODE = 3; // r

    private int counter;

    // Guards über "g" setzen
    // Alle Markierungen löschen über "c"


    // ScrollPane Action
    // entf -> remove selected items in list and SFC!!!
    // double click -> change (but not name!!!)
    
    
    private int mode = EDIT_MODE;
    
    private ESFCList     eSFCList;
    private ESFC         eSFC;
    private JTabbedPane  drawBoardTabbedPane = new JTabbedPane();
    private JScrollPane  guardList = new JScrollPane();
    private JScrollPane  actionList = new JScrollPane();
    private JScrollPane  declaringList = new JScrollPane();
    private JList        actionJList;
    private JList        guardJList;
    private MenuAndStatePanel menuAndStatePanel;
    private ScrollPane  helpPane;

    // Toggel to transition mode by pressing t
    // Toggel to step mode by pressing s
    // Toogel to remove mode by presseing r
    
    
    
    /**
     * Creates an editor with an 'empty' SFC. Do not use it yet.
     */
    public Editor() {
	this(new SFC());
    }

    /**
     * Creates an editor with the given SFC.
     */
    public Editor(SFC sfc) {
	this.menuAndStatePanel = new MenuAndStatePanel(this);
	this.drawBoardTabbedPane.addChangeListener(this);
	this.addHelpPane();
	this.eSFCList = new ESFCList();
	this.add(sfc);
	this.initWindow("SFC-Editor");
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
     * Adds the given sfc to the editor in a seperate DrawBoard.
     */
    public void add(SFC sfc) {
	this.eSFC = new ESFC(sfc);
	this.eSFC.setDrawBoard(new DrawBoard(this));
	this.eSFCList.add(this.eSFC);
	this.addTabbedPane(this.eSFC);
    }

    protected ESFC getExtendedSelectedSFC() {
	return this.eSFC;
    }

    /**
     * Returns the actual SFC used by the editor.
     */
    public SFC getSelectedSFC() {
	return this.eSFC.getSFC();
    }

    private void initWindow(String title) {
	this.setSize(WIDTH, HEIGHT);
	this.setBackground(Color.lightGray);
	this.setLayout(new BorderLayout());
	this.add(menuAndStatePanel, BorderLayout.SOUTH);
	this.add(this.getCenterPanel(), BorderLayout.CENTER);
	JPanel westPanel = new JPanel();
	westPanel.setLayout( new BorderLayout() );
	westPanel.add(this.guardList, BorderLayout.NORTH);
	westPanel.add(this.actionList, BorderLayout.CENTER);
	westPanel.add(this.declaringList, BorderLayout.SOUTH);
	this.add(westPanel, BorderLayout.WEST);
	this.updateWindow();
	this.setBorder(this.getTitledBorder("Editor"));
	this.setVisible(true);
    }

    private void updateWindow() {
	if (this.eSFC != null) {
	    this.getGuardList();
	    this.getActionList();
	    this.getDeclaringList();
	} else {
	    JList list = new JList();
	    list.setBackground(Color.lightGray);
	    list.setFixedCellWidth(150);
	    this.guardList.setViewportView(list);
	    list = new JList();
	    list.setBackground(Color.lightGray);
	    list.setFixedCellWidth(150);
	    this.actionList.setViewportView(list);
	    list = new JList();
	    list.setBackground(Color.lightGray);
	    list.setFixedCellWidth(150);
	    this.declaringList.setViewportView(list);
	}
    }

    private JScrollPane getGuardList() {
	Object[] o;
	SFC sfc	= this.eSFC.getSFC();
	if (sfc == null)
	    o = new Object[0];
	else {
	    LinkedList transitionList = sfc.transs;
	    LinkedList guards = new LinkedList();
	    for (int i=0; i<transitionList.size(); i++) {
		Transition transition = (Transition)transitionList.get(i);
		Expr guard = transition.guard;
		String name = this.eSFC.output(guard);
		if (!this.isStringInList(guards, name))
		    guards.add(name);
	    }
	    o = guards.toArray();
	}
	guardJList = new JList(o);
	guardJList.setBackground(Color.lightGray);
	guardJList.addMouseListener(this);
	guardList.setViewportView(guardJList); // = new JScrollPane(guardJList);
	guardList.setBorder(this.getTitledBorder("Guards"));
	guardList.setBackground(Color.lightGray);
	guardJList.setFixedCellWidth(150);
	return guardList;
    }

    private boolean isStringInList(LinkedList list, String string) {
	for (int i=0; i<list.size(); i++) 
	    if (list.get(i).toString().equals(string))
		return true;
	return false;
    }

    private JScrollPane getActionList() {
	Object[] o;
	SFC sfc = this.eSFC.getSFC();
	if (sfc == null)
	    o = new Object[0];
	else {
	    LinkedList aList = sfc.actions;
	    o = new Object[aList.size()];
	    for (int i=0; i< aList.size(); i++) {
		String s = this.eSFC.output((slime.absynt.Action)aList.get(i));
		o[i] = s;
	    }
	}
	this.actionJList = new JList(o);
	actionJList.setBackground(Color.lightGray);
	actionList.setViewportView(this.actionJList); // = new JScrollPane(this.actionJList);
	actionList.setBorder(this.getTitledBorder("Actions"));
	actionList.setBackground(Color.lightGray);
	actionJList.setFixedCellWidth(150);
	return actionList;
    }

    private JScrollPane getDeclaringList() {
	Object[] o;
	SFC sfc = this.eSFC.getSFC();
	if (sfc == null)
	    o = new Object[0];
	else {
	    LinkedList dList = sfc.declist;
	    o = new Object[dList.size()];
	    for (int i=0; i< dList.size(); i++) {
		String s = this.eSFC.output((slime.absynt.Declaration)dList.get(i));
		o[i] = s;
	    }
	}
	JList list = new JList(o);
	list.setBackground(Color.lightGray);
	declaringList.setViewportView(list); // = new JScrollPane(list);
	declaringList.setBorder(this.getTitledBorder("Declarations"));
	declaringList.setBackground(Color.lightGray);
	list.setFixedCellWidth(150);
	return declaringList;
    }    

    protected TitledBorder getTitledBorder(String title) {
	TitledBorder titledBorder = new TitledBorder(title);
	titledBorder.setTitleFont(new Font("Courier", Font.PLAIN, 14));
	titledBorder.setTitleColor(Color.blue);
	return titledBorder;
    }

    private JPanel getCenterPanel() {
	JPanel panel = new JPanel();
	panel.add(this.drawBoardTabbedPane);
	panel.setBackground(Color.lightGray);
	panel.setBorder(this.getTitledBorder("SFC's"));
	return panel;
    }

    private void addTabbedPane(ESFC eSFC) {
	ScrollPane jsp = new ScrollPane();
	jsp.setSize(640,480);
	jsp.add(eSFC.getDrawBoard());
	this.drawBoardTabbedPane.add("SFC "+counter++, jsp);
	this.drawBoardTabbedPane.setSelectedComponent(jsp);
    }

    private void  addHelpPane() {
	this.helpPane = new ScrollPane();
	helpPane.setSize(640,480);
	JTextArea tA = new JTextArea(20,40);
	tA.append("- Add a new step ...\n"
		  +"Leftclick on a free place on the drawboard.\n\n");
	tA.append("- Delete a step ...\n"
		  +"Switch with 'r' to REMOVE-mode and leftclick on the step.\n\n");
	tA.append("- Move a step ...\n"
		  +"Switch with 'e' to EDIT-mode and drag the step with the left mousebutton hold down.\n\n");
	tA.append("- Move a group of steps ...\n"
		  +"Mark the set of steps you wish to move as a set of source or target steps and move them like one step.\n\n");
	tA.append("- Change the name of a step ...\n"
		  +"Double click on the step.\n\n");
	tA.append("- Mark a step as initial ...\n"
		  +"Double click on the step and check the 'Initial step' button.\n\n");
	tA.append("- Mark a step as source ...\n"
		  +"Switch with 's' to SOURCE-mode and leftclick on the step.\n\n");
	tA.append("- Mark a step as target ...\n"
		  +"Switch with 't' to TARGET-mode and leftclick on the step.\n\n");
	tA.append("- Add transition(s) between marked source and target steps ...\n"
		  +"Press 'g'. Actually there is no possibility to choose a guard. Wait for later versions :-).\n\n");
	tA.setEnabled(false);
	tA.setWrapStyleWord(true);
	tA.setLineWrap(true);
	tA.setFont(new Font("Courier", Font.PLAIN, 14));
	helpPane.add(tA);
	this.drawBoardTabbedPane.add("Help", helpPane);
	this.drawBoardTabbedPane.setSelectedComponent(helpPane);
    }
    
    protected void evaluateDoubleMouseClickOn(int x, int y) {
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	if (step != null) {
	    this.menuAndStatePanel.setStatusMessage("Change step attributes.");
	    this.stepDialogWindow("Change step ...", step, x, y);
	}
    }

    private void stepDialogWindow(String title, Step step, int x, int y) {

	Object[] o;
	SFC sfc = this.eSFC.getSFC();
	if (sfc == null)
	    o = new Object[0];
	else {
	    LinkedList aList = sfc.actions;
	    o = new Object[aList.size()];
	    for (int i=0; i< aList.size(); i++) {
		String s = this.eSFC.output((slime.absynt.Action)aList.get(i));
		o[i] = s;
	    }
	}
	JList actionJList = new JList(o);
	JScrollPane actionList = new JScrollPane(actionJList);
	

	int[] selectedIndices = this.getActionIndices(step);
	actionJList.setSelectedIndices(selectedIndices);

	boolean isIStep = (step == this.eSFC.getSFC().istep);
	Object[] message = new Object[7];
	String name = "";
	if (step != null)
	    name = step.name;
	message[0] = "Name";
	message[1] = (new JTextField(name));
	message[2] = (new JRadioButton("Initial step", isIStep));
	message[3] = "SAP";
	message[4] = (new JTextField(""));
	message[5] = "Existing actions";
	message[6] = (actionList);
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
	    if (((JRadioButton)message[2]).isSelected())
		this.eSFC.getSFC().istep = step;
	    else
		if (isIStep)
		    this.eSFC.getSFC().istep = null;
	    String sapString = ((JTextField)message[4]).getText();
	    LinkedList sap = new LinkedList();

	    // FIX ME: Name schon vorhanden ??
	    // FIX ME: Name muss gewaehlt werden koennen
	    // FIX ME: Neue Variablen muessen eingefuegt werden
	    // FIX ME: Testen, ob Varaiblen schon vorhanden sind und ob Typ gleich ist.
	    if (!sapString.equals("")) {
		slime.utils.Parser parser = new slime.utils.Parser();
		try {
		    sap = parser.parseSAP(sapString);
		} catch (ParseException e) {
		    this.menuAndStatePanel.setStatusMessage("Parser fails on SAP");
		} catch (Exception e) {
		    this.menuAndStatePanel.setStatusMessage("Oh no !!!!");
		}
	    }
	    step.actions = this.getStepActions(actionJList);
	    if (sap.size() != 0) {
		slime.absynt.Action action = new slime.absynt.Action(((JTextField)message[1]).getText(), sap);
		step.actions.add(new StepAction(new Nqual(), ((JTextField)message[1]).getText()));
		this.eSFC.getSFC().actions.add(action);
		this.updateWindow();
	    }
		
	}
	//this.pack();
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
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	if (step != null) {
	    if (mode == REMOVE_MODE) {
		this.eSFC.removeStep(step);
		this.updateWindow();
		this.menuAndStatePanel.setStatusMessage("Step "+step.name+" removed.");
		this.menuAndStatePanel.enableButtons();
	    }
	    else if (mode == SOURCE_MODE) 
		if (this.eSFC.isSourceStep(step))
		    this.eSFC.removeSourceStep(step);
		else
		    this.eSFC.addSourceStep(step);
	    else if (mode == TARGET_MODE)
		if (this.eSFC.isTargetStep(step))
		    this.eSFC.removeTargetStep(step);
		else
		    this.eSFC.addTargetStep(step);
	}
	else {
	    this.menuAndStatePanel.setStatusMessage("Adding a new step.");
	    this.stepDialogWindow("New step ...", null, x, y);
	}
    }
    
    protected void evaluateMouseDragged(int x0, int y0, int x1, int y1) {
	if (mode == EDIT_MODE) {
	    Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x0, y0);
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
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	Object o = this.eSFC.getSelectedObject();
	this.eSFC.setSelectedObject(step);
	if (step != o) {
	    int[] selectedIndices = this.getActionIndices(step);
	    this.actionJList.setSelectedIndices(selectedIndices);
	}
	return (step != o);
    }

    private Step determineSelectedStep(LinkedList stepList, int x, int y) {
	int stepNumber = 0;
	while (stepNumber < stepList.size()) {
	    Step step = (Step)stepList.get(stepNumber);
	    if (isInStep(step, x, y))
		return step;
	    stepNumber += 1;
	}
	return null;
    }
    
    private boolean isInStep(Step step, int x, int y) {
	Position position = step.pos;
	int xOfStep = (int)position.x;
	int yOfStep = (int)position.y;
	int stepWidth = this.eSFC.getWidth(step);
	if (x < xOfStep) return false;
	if (y < yOfStep) return false;
	if (x > (xOfStep + stepWidth)) return false;
	if (y > (yOfStep + 30)) return false;
	return true;
    }

    protected int toggleMode(int newMode) {
	int oldMode = this.mode;
	this.mode = newMode;
	return oldMode;
    }

    protected void evaluateKeyTyped(char c) {
	if (c == (new String("e")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Toggled to EDIT-mode");
	    this.toggleMode(EDIT_MODE);
	}
	if (c == (new String("s")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Toggled to SOURCE-mode");
	    this.toggleMode(SOURCE_MODE);
	}
	if (c == (new String("t")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Toggled to TARGET-mode");
	    this.toggleMode(TARGET_MODE);
	}
	if (c == (new String("r")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Toggled to REMOVE-mode");
	    this.toggleMode(REMOVE_MODE);
	}
	if (c == (new String("c")).charAt(0)) {
	    this.menuAndStatePanel.setStatusMessage("Removed als marks");
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

	    Object[] o;
	    SFC sfc = this.eSFC.getSFC();
	    if (sfc == null)
		o = new Object[0];
	    else {
		LinkedList transitionList = sfc.transs;
		LinkedList guards = new LinkedList();
		for (int i=0; i<transitionList.size(); i++) {
		    Transition transition = (Transition)transitionList.get(i);
		    Expr guard = transition.guard;
		    String name = this.eSFC.output(guard);
		    if (!this.isStringInList(guards, name))
			guards.add(name);
		}
	    o = guards.toArray();
	    }
	    JList guardJList = new JList(o);
	    guardJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    JScrollPane guardList = new JScrollPane(guardJList);
	    
	    Object[] message = new Object[4];
	    message[0] = "Expression";
	    message[1] = (new JTextField(""));
	    message[2] = "Existing guards";
	    message[3] = (guardList);
	    String[] options = {"Ok", "Cancel"};
	    
	    int result = JOptionPane.showOptionDialog(
		null,
		message,
		"Add new transition",
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.PLAIN_MESSAGE,
		null,
		options,
		options[0]);
	    if (result == 0) {
		this.eSFC.setChecked(false);
		this.menuAndStatePanel.enableButtons();
		if (((JTextField)message[1]).getText().equals("")) {
		    String name = (String)guardJList.getSelectedValue();
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
			this.menuAndStatePanel.setStatusMessage("New transition(s) added");
			this.eSFC.addTransition(this.eSFC.getSourceSteps(), 
						eGuard, 
						this.eSFC.getTargetSteps());
			this.toggleMode(SOURCE_MODE);
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
	    
	    if (expr != null) {
		
		this.menuAndStatePanel.setStatusMessage("New transition(s) added");
		this.eSFC.addTransition(this.eSFC.getSourceSteps(), 
					expr, 
					this.eSFC.getTargetSteps());
		this.toggleMode(SOURCE_MODE);
		this.updateWindow();
	    }
	    
	    //this.menuAndStatePanel.setStatusMessage("Expression input not parsed yet");
		}
	    }
	}
    }

    public void mouseClicked(MouseEvent e) {
	e.consume();
	if (this.eSFC == null)
	    return;
	Object[] selectedValues = this.guardJList.getSelectedValues();
	LinkedList transitionList = this.eSFC.getSFC().transs;
	for (int i=0; i<transitionList.size(); i++) {
	    Transition transition = (Transition)transitionList.get(i);
	    this.eSFC.deHighlight(transition);
	    Expr guard = transition.guard;
	    String name = this.eSFC.output(guard);
	    for (int j=0; j<selectedValues.length; j++) {
		String name2 = (String)selectedValues[j];
		if (name2.equals(name))
		    this.eSFC.highlight(transition, Color.green);
	    }
	}
	ScrollPane c = (ScrollPane)this.drawBoardTabbedPane.getSelectedComponent();
	DrawBoard d = (DrawBoard)c.getComponent(0); // FIX ME: So nicht, aber es geht :-)
	d.repaint();
    }

    public void mouseEntered(MouseEvent e) {
	this.requestFocus();
	e.consume();
    }

    public void mouseExited(MouseEvent e) {
	e.consume();
    }

    public void mousePressed(MouseEvent e) {
	e.consume();
    }

    public void mouseReleased(MouseEvent e) {
	e.consume();
    }

}



















