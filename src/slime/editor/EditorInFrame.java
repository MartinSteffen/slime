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
 * @author Andreas Niemann
 * @version $Id: EditorInFrame.java,v 1.2 2002-06-06 16:10:14 swprakt Exp $
 */

public final class EditorInFrame extends JFrame {

    private Editor editor;

   
    /**
     * Creates an editor with an 'empty' SFC. Do not use it yet.
     */
    private EditorInFrame() {
	this(new Editor());
    }

    /**
     * Creates an editor with the given SFC.
     */
    private EditorInFrame(Editor editor) {
	this.editor = editor;
	this.initWindow("SFC-Editor v.0.2");
    }

    private void initWindow(String title) {
	this.setSize(WIDTH, HEIGHT);
	this.setBackground(Color.white);
	this.setTitle(title);
	this.getContentPane().setLayout( new BorderLayout() );
	this.getContentPane().add(this.getCenterPanel(), BorderLayout.CENTER);
	this.getContentPane().add(this.getCenterPanel(), BorderLayout.SOUTH);
	this.setVisible(true);
	this.addWindowListener( new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit( 0 );
		}
	    });
	this.pack();
    }

    private Panel getCenterPanel() {
	Panel panel = new Panel();
	panel.add(this.editor);
	return panel;
    }

    public static void main(String[] argv) {
	SFC sfc1 = Example.getExample1();
	SFC sfc2 = Example.getExample1();
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























