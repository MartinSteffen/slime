package editor;

import absynt.*;
import layout.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * <BR> <BR>
 * Feel free to play around with this initial version of an SFC-editor.  
 * @author Andreas Niemann
 * @version $Id: Editor.java,v 1.5 2002-06-06 16:10:14 swprakt Exp $
 */

public final class Editor extends JComponent implements ChangeListener{

    private static final int WIDTH = 100, HEIGHT = 300;

    private static final int EDIT_MODE   = 0; // e
    private static final int SOURCE_MODE = 1; // s
    private static final int TARGET_MODE = 2; // t
    private static final int REMOVE_MODE = 3; // r
    // Guards über "g" setzen
    // Alle Markierungen löschen über "c"
    
    private int mode = EDIT_MODE;
    
    private ESFCList     eSFCList;
    private ESFC         eSFC;
    private JTextField   statusMessage;
    private JTabbedPane  drawBoardTabbedPane = new JTabbedPane();

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
	//this.pack();
    }

    public void stateChanged(ChangeEvent e) {
	ScrollPane c = (ScrollPane)this.drawBoardTabbedPane.getSelectedComponent();
	c.repaint();
	DrawBoard d = (DrawBoard)c.getComponent(0); // FIX ME: So nicht, aber es geht :-)
	this.eSFC = d.getESFC();
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
	this.setBackground(Color.gray);
	//this.setTitle(title);
	this.setLayout( new BorderLayout() );
	this.add(this.getCenterPanel(), BorderLayout.CENTER);
	this.add(this.getSouthPanel(), BorderLayout.SOUTH);
	this.setVisible(true);
//  	this.addWindowListener( new WindowAdapter() {
//  		public void windowClosing(WindowEvent e) {
//  		    System.exit( 0 );
//  		}
//  	    });
    }

    private Panel getCenterPanel() {
	Panel panel = new Panel();
	panel.add(this.drawBoardTabbedPane);
	return panel;
    }

    private void addTabbedPane(ESFC eSFC) {
	ScrollPane jsp = new ScrollPane();
	jsp.setSize(800,600);
	jsp.add(eSFC.getDrawBoard());
	this.drawBoardTabbedPane.add(jsp);
	this.drawBoardTabbedPane.setSelectedComponent(jsp);
    }

    private JPanel getSouthPanel() {
	JPanel panel = new JPanel();
	this.statusMessage = new JTextField( 40 );
	panel.add( statusMessage );
	panel.add(this.getCheckButton());
	panel.add(this.getLayoutButton());
	panel.add(this.getHelpButton());
	panel.add(this.getCloseButton());
	return panel;
    }

    private Button getCloseButton() {
	Button close = new Button("Close");
	close.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    System.exit(0);
		}
	    });
	return close;
    }

    private Button getHelpButton() {
	Button help = new Button("Help");
	help.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Showing some help.");
		    Object[] message = new Object[7];
		    message[0] = "- Add a new step ...\n"
			+"   Leftclick on a free place on the drawboard.\n";
		    message[1] = "- Delete a step ...\n"
			+"   Switch with 'r' to REMOVE-mode and leftclick on the step.\n";
		    message[2] = "- Move a step ...\n"
			+"   Switch with 'e' to EDIT-mode and drag the step with the left mousebutton hold down.\n";
		    message[3] = "- Change the name of a step ...\n"
			+"   Double click on the step.\n";
		    message[4] = "- Mark a step as source ...\n"
			+"   Switch with 's' to SOURCE-mode and leftclick on the step.\n";
		    message[5] = "- Mark a step as target ...\n"
			+"   Switch with 't' to TARGET-mode and leftclick on the step.\n";
		    message[6] = "- Add transition(s) between marked source and target steps ...\n"
			+"   Press 'g'. Actually there is no possibility to choose a guard. Wait for later versions :-).\n";

		    String[] options = {"Ok"};
		    int result = JOptionPane.showOptionDialog(
			null,
			message,
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

    private Button getLayoutButton() {
	Button layout = new Button("Layout");
	layout.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Layouter trys his best at his actual implementation ...");
		    Layouter.position_sfc(eSFC.getSFC());
		}
	    });
	return layout;
    }

    private Button getCheckButton() {
	Button check = new Button("Check");
	check.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Checker not yet implemented.");
		}
	    });
	return check;
    }
    
    protected void evaluateDoubleMouseClickOn(int x, int y) {
	Step step = this.determineSelectedStep(this.eSFC.getSFC().steps, x, y);
	if (step != null) {
	    statusMessage.setText("Awaiting a new name for the step..");
	    Object[] message = new Object[2];
	    message[0] = "Name";
	    message[1] = (new JTextField(step.name));
	    String[] options = {"Ok", "Cancel"};

	    int result = JOptionPane.showOptionDialog(
		null,
		message,
		"Change name ...",
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE,
		null,
		options,
		options[0]);
	    if (result == 0)
		step.name = ((JTextField)message[1]).getText();
	}
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
	    Object[] message = new Object[2];
	    message[0] = "Name";
	    message[1] = new JTextField();
	    String[] options = {"Add", "Cancel"};
	    
	    int result = JOptionPane.showOptionDialog(
		null,
		message,
		"Add new step ...",
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE,
		null,
		options,
		options[0]);
	    if (result == 0)
		this.eSFC.addStep(((JTextField)message[1]).getText(), x, y);
	}
	//this.pack();
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
	    this.statusMessage.setText("New transition(s) added");
	    this.eSFC.addTransition(this.eSFC.getSourceSteps(), 
				    null, 
				    this.eSFC.getTargetSteps());
	    this.toggleMode(SOURCE_MODE);
	}
	//this.pack();
    }

//      public static void main(String[] argv) {
//  	SFC sfc1 = Example.getExample1();
//  	SFC sfc2 = Example.getExample1();
//  	for (int i=0; i < sfc1.steps.size(); i++) {
//  	    Step step = (Step)(sfc1.steps).get(i);
//  	    step.pos = new Position((float)(i*40),40.0f);
//  	}
//  	for (int i=0; i < sfc2.steps.size(); i++) {
//  	    Step step = (Step)(sfc2.steps).get(i);
//  	    step.pos = new Position((float)(i*40),40.0f);
//  	}
//  	Editor editor = new Editor(sfc1);
//  	editor.add(sfc2);
//      }
}























