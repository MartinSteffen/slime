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
 * @version $Id: DeclarationListListener.java,v 1.1 2002-07-08 13:56:02 swprakt Exp $
 */

public final class DeclarationListListener implements MouseListener {

    private Editor editor;

    protected DeclarationListListener(Editor editor) {
	this.editor = editor;
    }

    public void mouseClicked(MouseEvent e) {
	e.consume();
	if (e.getClickCount() > 1)
	    this.editor.reactOnChangeOfDeclaration();
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













