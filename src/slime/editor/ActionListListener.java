package slime.editor;

import slime.absynt.*;
import slime.editor.*;

import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * <br><br>
 * Status: complete <br> 
 * Known bugs: - <br>
 * @author Andreas Niemann
 * @version $Id: ActionListListener.java,v 1.1 2002-06-20 11:25:05 swprakt Exp $
 */

public final class ActionListListener implements MouseListener {

    private Editor editor;

    protected ActionListListener(Editor editor) {
	this.editor = editor;
    }

    public void mouseClicked(MouseEvent e) {
	e.consume();
	if (e.getClickCount() > 1)
	    this.editor.reactOnChangeOfAction();
    }
    
    public void mouseEntered(MouseEvent e) {
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













