package editor;

import absynt.*;
import layout.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * <BR> <BR>
 * Feel free to play around with this initial version of an SFC-editor. Initially only one SFC is used at the same time, but that will be changed in the near future.  
 * @author Andreas Niemann
 * @version $Id: Editor.java,v 1.1 2002-05-22 12:25:06 swprakt Exp $
 */

public final class Editor extends JFrame {

    private static final int WIDTH = 100, HEIGHT = 300, NO_STEP = -1;

    private SFC          sfc;
    private SFCPainter   sfcPainter;
    private DrawBoard    drawBoard;
    private JTextField   statusMessage;
    private JTabbedPane  tabbedPane;
    private int          selectedStep = NO_STEP;

    public Editor() {
	this(new SFC());
    }

    public Editor(SFC sfc) {
	this.sfc        = sfc;
	this.sfcPainter = new SFCPainter(this, sfc);
	this.initWindow("SFC-Editor v.0.1");
	this.pack();
    }

    protected SFC getSFC() {
	return this.sfc;
    }

    protected void setStatusMessage(String message) {
	this.statusMessage.setText(message);
    }

    protected SFCPainter getSFCPainter() {
	return this.sfcPainter;
    }

    protected int getSelectedStep() {
	return this.selectedStep;
    }

    private void initWindow(String title) {
	this.setSize(WIDTH, HEIGHT);
	this.setBackground(Color.gray);
	this.setTitle(title);
	this.getContentPane().setLayout( new BorderLayout() );
	this.getContentPane().add(this.getCenterPanel(), BorderLayout.CENTER);
	this.getContentPane().add(this.getSouthPanel(), BorderLayout.SOUTH);
	this.setVisible(true);
	this.addWindowListener( new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit( 0 );
		}
	    });
    }

    private Panel getCenterPanel() {
	Panel panel = new Panel();
	tabbedPane = new JTabbedPane();
	ScrollPane jsp = new ScrollPane();
	jsp.setSize(800,600);
	this.drawBoard = new DrawBoard(this);
	jsp.add(this.drawBoard);
	tabbedPane.add("SFC",jsp);
	panel.add(tabbedPane);
	//panel.add(jsp);
	return panel;
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
		    Object[] message = new Object[4];
		    message[0] = "- Add a new step ...\n"
			+"   Leftclick on a free place on the drawboard.\n";
		    message[1] = "- Delete a step ...\n"
			+"   Mark the step with the left mousebutton and press 'd'.\n";
		    message[2] = "- Move a step ...\n"
			+"   Drag the step with the left mousebutton hold down.\n";
		    message[3] = "- Change name of step ...\n"
			+"   Double click on the step.\n";
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
		    Layouter.position_sfc(sfc);
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
    
    protected void evaluateSingleMousePressedOn(int x, int y) {
	this.selectedStep = this.determineSelectedStep(x, y);
    }

    protected void evaluateDoubleMouseClickOn(int x, int y) {
	if (this.selectedStep != NO_STEP) {
	    statusMessage.setText("Awaiting a new name for the step..");
	    Step step = (Step)this.sfc.steps.get(this.selectedStep);
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
	if (this.selectedStep == NO_STEP) {
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
		this.addNewStep(x, y, ((JTextField)message[1]).getText());
	}
    }

    protected void evaluateMouseDragged(int x0, int y0, int x1, int y1) {
	if (this.selectedStep != NO_STEP) {
	    int deltaX = x1 - x0;
	    int deltaY = y1 - y0;
	    Position position = ((Step)(this.sfc.steps).get(this.selectedStep)).pos;
	    int newX = (int)position.x + deltaX;
	    int newY = (int)position.y + deltaY;
	    position.x = (float)newX;
	    position.y = (float)newY;
	    this.pack();
	}    
    }

    private int determineSelectedStep(int x, int y) {
	int stepNumber = 0;
	while (stepNumber < this.sfc.steps.size()) {
	    if (isInStep((Step)(this.sfc.steps).get(stepNumber), x, y))
		return stepNumber;
	    stepNumber += 1;
	}
	return NO_STEP;
    }
    
    private boolean isInStep(Step step, int x, int y) {
	Position position = step.pos;
	int xOfStep = (int)position.x;
	int yOfStep = (int)position.y;
	if (x < xOfStep) return false;
	if (y < yOfStep) return false;
	if (x > (xOfStep + 30)) return false;
	if (y > (yOfStep + 30)) return false;
	return true;
    }

    public void processDelete() {
	if (this.selectedStep == NO_STEP)
	    return;
	LinkedList transitionList = this.sfc.transs;
	for (int i=0; i<transitionList.size(); i++) {
	    Transition transition = (Transition)transitionList.get(i);
	    LinkedList source = transition.source;
	    LinkedList target = transition.target;
	    Step step = (Step)this.sfc.steps.get(this.selectedStep);
	    for (int s=0; s<source.size(); s++) {
		Step sStep = (Step)source.get(s);
		if (sStep == step)
		    source.remove(s--);
	    }
	    for (int t=0; t<target.size(); t++) {
		Step tStep = (Step)target.get(t);
		if (tStep == step)
		    target.remove(t--);
	    }
	    if ((source.size() == 0) || (target.size() == 0))
		transitionList.remove(i--);
	}
	this.sfc.steps.remove(selectedStep);
	this.selectedStep = NO_STEP;
	this.pack();
    }

    private void addNewStep(int x, int y, String name) {
	Step step = new Step(name);
	Position position = new Position((float)x, (float)y);
	step.pos = position;
	this.sfc.steps.add(step);
    }

    public static void main(String[] argv) {
	SFC sfc1 = Example.getExample1();
	for (int i=0; i < sfc1.steps.size(); i++) {
	    Step step = (Step)(sfc1.steps).get(i);
	    step.pos = new Position((float)(i*40),40.0f);
	}
	new Editor(sfc1);
    }
}























