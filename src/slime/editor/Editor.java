package editor;

import absynt.*;
import layout.*;
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
 * @version $Id: Editor.java,v 1.9 2002-06-10 18:07:23 swprakt Exp $
 */

public final class Editor extends JComponent 
    implements ChangeListener, MouseListener {

    private static final int WIDTH = 100, HEIGHT = 300;

    private static final int EDIT_MODE   = 0; // e
    private static final int SOURCE_MODE = 1; // s
    private static final int TARGET_MODE = 2; // t
    private static final int REMOVE_MODE = 3; // r
    // Guards über "g" setzen
    // Alle Markierungen löschen über "c"


    // ScrollPane Action
    // entf -> remove selected items in list and SFC!!!
    // double click -> change (but not name!!!)
    
    
    private int mode = EDIT_MODE;
    
    private ESFCList     eSFCList;
    private ESFC         eSFC;
    private JTextField   statusMessage;
    private JTabbedPane  drawBoardTabbedPane = new JTabbedPane();
    private JScrollPane  guardList;
    private JScrollPane  actionList;
    private JScrollPane  declaringList;
    private JList        actionJList;
    private JList        guardJList;

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
	this.drawBoardTabbedPane.addChangeListener(this);
	this.eSFCList = new ESFCList();
	this.add(sfc);
	this.initWindow("SFC-Editor");
    }

    public void stateChanged(ChangeEvent e) {
	ScrollPane c = (ScrollPane)this.drawBoardTabbedPane.getSelectedComponent();
	c.repaint();
	DrawBoard d = (DrawBoard)c.getComponent(0); // FIX ME: So nicht, aber es geht :-)
	this.eSFC = d.getESFC();
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

    protected void setStatusMessage(String message) {
	this.statusMessage.setText(message);
    }

    private void initWindow(String title) {
	this.setSize(WIDTH, HEIGHT);
	this.setBackground(Color.lightGray);
	this.setVisible(true);
	this.setLayout( new BorderLayout() );
	this.add(this.getCenterPanel(), BorderLayout.CENTER);
	this.add(this.getSouthPanel(), BorderLayout.SOUTH);
	this.updateWindow();
    }

    private void updateWindow() {
	this.add(this.getWestPanel(), BorderLayout.WEST);
    }


    private JPanel getWestPanel() {
	JPanel panel = new JPanel();
	panel.setLayout( new BorderLayout() );
	panel.add(this.getGuardList(), BorderLayout.NORTH);
	panel.add(this.getActionList(), BorderLayout.CENTER);
	panel.add(this.getDeclaringList(), BorderLayout.SOUTH);
	return panel;
    }

    private JScrollPane getGuardList() {
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
	guardJList = new JList(o);
	guardJList.addMouseListener(this);
	guardJList.setBackground(this.getBackground());
	guardList = new JScrollPane(guardJList);
 	guardList.setBorder(new TitledBorder("Guards")); 
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
		String s = this.eSFC.output((absynt.Action)aList.get(i));
		o[i] = s;
	    }
	}
	this.actionJList = new JList(o);
	actionJList.setBackground(this.getBackground());
	actionList = new JScrollPane(this.actionJList);
 	actionList.setBorder(new TitledBorder("Actions")); 
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
		String s = this.eSFC.output((absynt.Declaration)dList.get(i));
		o[i] = s;
	    }
	}	
	JList list = new JList(o);
	list.setBackground(this.getBackground());
	declaringList = new JScrollPane(list);
 	declaringList.setBorder(new TitledBorder("Declarations")); 
	return declaringList;
    }    

    private JPanel getCenterPanel() {
	JPanel panel = new JPanel();
	panel.add(this.drawBoardTabbedPane);
	return panel;
    }

    private void addTabbedPane(ESFC eSFC) {
	ScrollPane jsp = new ScrollPane();
	jsp.setSize(900,600);
	jsp.add(eSFC.getDrawBoard());
	this.drawBoardTabbedPane.add(jsp);
	this.drawBoardTabbedPane.setSelectedComponent(jsp);
    }

    private JPanel getSouthPanel() {
	JPanel panel = new JPanel();
	this.statusMessage = new JTextField( 40 );
	panel.setLayout(new FlowLayout());
	panel.add(this.getGuardButton());
	panel.add(this.getActionButton());
	panel.add(this.getVariableButton());
	panel.add( statusMessage );
	panel.add(this.getCheckButton());
	panel.add(this.getLayoutButton());
	panel.add(this.getSimulateButton());
	panel.add(this.getHelpButton());
	panel.add(this.getCloseButton());
	return panel;
    }

    private JButton getCloseButton() {
	JButton close = new JButton("Close");
	close.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    System.exit(0);
		}
	    });
	return close;
    }

    private JButton getHelpButton() {
	JButton help = new JButton("Help");
	help.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Showing some help.");
		    Object[] message = new Object[9];
		    message[0] = "- Add a new step ...\n"
			+"   Leftclick on a free place on the drawboard.\n";
		    message[1] = "- Delete a step ...\n"
			+"   Switch with 'r' to REMOVE-mode and leftclick on the step.\n";
		    message[2] = "- Move a step ...\n"
			+"   Switch with 'e' to EDIT-mode and drag the step with the left mousebutton hold down.\n";
		    message[3] = "- Move a group of steps ...\n"
			+"   Mark the set of steps you wish to move as a set of source or target steps and move them like one step.\n";
		    message[4] = "- Change the name of a step ...\n"
			+"   Double click on the step.\n";
		    message[5] = "- Mark a step as initial ...\n"
			+"   Double click on the step and check the 'Initial step' button.\n";
		    message[6] = "- Mark a step as source ...\n"
			+"   Switch with 's' to SOURCE-mode and leftclick on the step.\n";
		    message[7] = "- Mark a step as target ...\n"
			+"   Switch with 't' to TARGET-mode and leftclick on the step.\n";
		    message[8] = "- Add transition(s) between marked source and target steps ...\n"
			+"   Press 'g'. Actually there is no possibility to choose a guard. Wait for later versions :-).\n";

		    String[] options = {"Ok"};
		    int result = JOptionPane.showOptionDialog(
			null,
			message, //new JScrollPane(new JList(message)),
			"Need some help?",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			options,
			options[0]);
		}
	    });
	return help;
    }

    private JButton getSimulateButton() {
	JButton simulate = new JButton("Simulate");
	simulate.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Simulator not yet implemented.");
		}
	    });
	return simulate;
    }

    private JButton getLayoutButton() {
	JButton layout = new JButton("Layout");
	layout.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Layouter trys his best at his actual implementation ...");
		    Layouter.position_sfc(eSFC.getSFC());
		}
	    });
	return layout;
    }

    private JButton getCheckButton() {
	JButton check = new JButton("Check");
	check.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Checker not yet implemented.");
		}
	    });
	return check;
    }

    private JButton getVariableButton() {
	JButton variable = new JButton("Variable");
	variable.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new variable.");
		}
	    });
	return variable;
    }

    private JButton getActionButton() {
	JButton action = new JButton("Action");
	action.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new action.");
		}
	    });
	return action;
    }

    private JButton getGuardButton() {
	JButton guard = new JButton("Guard");
	guard.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new guard.");
		}
	    });
	return guard;
    }
    
    protected void evaluateDoubleMouseClickOn(int x, int y) {
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	if (step != null) {
	    statusMessage.setText("Change step attributes.");
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
		String s = this.eSFC.output((absynt.Action)aList.get(i));
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
	message[3] = "Action";
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
	    if (step == null)
		step = this.eSFC.addStep(((JTextField)message[1]).getText(), x, y);
	    step.name = ((JTextField)message[1]).getText();
	    if (((JRadioButton)message[2]).isSelected())
		this.eSFC.getSFC().istep = step;
	    else
		if (isIStep)
		    this.eSFC.getSFC().istep = null;
	    if (!((JTextField)message[4]).getText().equals(""))
		statusMessage.setText("SAP's are not parsed yet");
	    step.actions = this.getStepActions(actionJList);
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
		absynt.Action action = (absynt.Action)actionList.get(j);
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
	    absynt.Action action = (absynt.Action)actionList.get(selectedIndices[i]);
	    stepAction.add(new StepAction(new Nqual(), action.a_name));
	}
	return stepAction;
    }

    protected void evaluateSingleMouseClickOn(int x, int y) {
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	if (step != null) {
	    if (mode == REMOVE_MODE) {
		this.eSFC.removeStep(step);
		this.statusMessage.setText("Step "+step.name+" removed.");
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
	    statusMessage.setText("Adding a new step.");
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
		//this.pack();
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
	    this.statusMessage.setText("Toggled to EDIT-mode");
	    this.toggleMode(EDIT_MODE);
	}
	if (c == (new String("s")).charAt(0)) {
	    this.statusMessage.setText("Toggled to SOURCE-mode");
	    this.toggleMode(SOURCE_MODE);
	}
	if (c == (new String("t")).charAt(0)) {
	    this.statusMessage.setText("Toggled to TARGET-mode");
	    this.toggleMode(TARGET_MODE);
	}
	if (c == (new String("r")).charAt(0)) {
	    this.statusMessage.setText("Toggled to REMOVE-mode");
	    this.toggleMode(REMOVE_MODE);
	}
	if (c == (new String("c")).charAt(0)) {
	    this.statusMessage.setText("Removed als marks");
	    this.eSFC.clearSourceSteps();
	    this.eSFC.clearTargetSteps();
	}
	if (c == (new String("g")).charAt(0)) {
	    if (this.eSFC.getNumberOfSourceSteps() == 0) {
		this.statusMessage.setText("No source step(s) selected");
		return;
	    }
	    if (this.eSFC.getNumberOfTargetSteps() == 0) {
		this.statusMessage.setText("No target step(s) selected");
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
		if (((JTextField)message[1]).getText().equals("")) {
		    String name = (String)guardJList.getSelectedValue();
		    if (name == null) {
			this.statusMessage.setText("No guard selected");
		    } else {
			Expr eGuard = null; 
			LinkedList transitionList = this.eSFC.getSFC().transs;
			for (int i=0; i<transitionList.size(); i++) {
			    Transition transition = (Transition)transitionList.get(i);
			    Expr guard = transition.guard;
			    if (this.eSFC.output(guard).equals(name))
				eGuard = guard;
			}
			this.statusMessage.setText("New transition(s) added");
			this.eSFC.addTransition(this.eSFC.getSourceSteps(), 
						eGuard, 
						this.eSFC.getTargetSteps());
			this.toggleMode(SOURCE_MODE);
		    }
		} else {
		    this.statusMessage.setText("Expression input not parsed yet");
		}
	    }
	}
    }

    public void mouseClicked(MouseEvent e) {
	e.consume();
	this.statusMessage.setText(":-(");
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
	this.statusMessage.setText(":-)");
    }

    public void mouseExited(MouseEvent e) {
	e.consume();
    }

    public void mousePressed(MouseEvent e) {
	e.consume();
	this.statusMessage.setText(":-(");
    }

    public void mouseReleased(MouseEvent e) {
	e.consume();
	this.statusMessage.setText(":-(");
    }

}



















