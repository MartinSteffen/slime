package slime;

import slime.absynt.*;
import slime.layout.*;
import slime.editor.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Top-level file for the Slime project of the Fortgeschrittenen-Praktikum.
 * <br><br>
 * @author Andreas Niemann
 * @version $Id: Main.java,v 1.2 2002-07-18 08:35:13 swprakt Exp $
 */


/* This is the former class editor.EditorInFrame, renamed and
   put into a more prominent place.

*/

public final class Main extends JFrame {

    private Editor editor;
   
    private Main(Editor editor) {
	this.editor = editor;
	this.initWindow("SFC-Editor v.1.0");
    }

    private void initWindow(String title) {
	this.setSize(10, 10);
	this.setBackground(Color.lightGray);
	this.setTitle(title);
	this.getContentPane().add(this.getCenterPanel());
	this.addWindowListener( new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit( 0 );
		}
	    });
	this.pack();
	this.setVisible(true);
    }

    private JPanel getCenterPanel() {
	JPanel panel = new JPanel();
	panel.add(this.editor);
	panel.setBackground(Color.lightGray);
	return panel;
    }

    public void paint(Graphics g) {
	super.paint(g);
	this.pack();
    }

    public static void main(String[] argv) {
	SFC sfc1 = Example.getExample1();
	SFC sfc2 = Example.getExample1();
	BoolType btype = new BoolType();
	Variable v_x = new Variable ("a", btype);
	Constval cfalse = new Constval (false);
	Declaration dec_x = new Declaration(v_x, btype, cfalse);
	sfc2.declist.add(dec_x);
	for (int i=0; i < sfc1.steps.size(); i++) {
	    Step step = (Step)(sfc1.steps).get(i);
	    step.pos = new Position((float)(i*40), (float)(i*40));
	}
	for (int i=0; i < sfc2.steps.size(); i++) {
	    Step step = (Step)(sfc2.steps).get(i);
	    step.pos = new Position((float)(i*40), (float)(i*40));
	}
	Editor editor = new Editor(sfc1);
	editor.add(sfc2);
	new Main(editor);
    }

}























