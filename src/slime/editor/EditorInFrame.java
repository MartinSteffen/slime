package slime.editor;

import slime.absynt.*;
import slime.layout.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * <br><br>
 * Status: always complete, since it is for testing only<br>
 * @author Andreas Niemann
 * @version $Id: EditorInFrame.java,v 1.12 2002-07-09 23:37:24 swprakt Exp $
 */

public final class EditorInFrame extends JFrame {

    private Editor editor;
   
    private EditorInFrame(Editor editor) {
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
	new EditorInFrame(editor);
    }

}























