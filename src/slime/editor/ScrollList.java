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
 * @version $Id: ScrollList.java,v 1.3 2002-07-09 23:37:24 swprakt Exp $
 */

public final class ScrollList extends JPanel{

    private JScrollPane scrollList;
    private JList       list;
    private JButton     button;

    /**
     * Constructs a scroll list with an empty list.
     */
    protected ScrollList(Color color) {
	this(color, new LinkedList());
    }

    /**
     * Constructs a scroll list with the given list. 
     */
    protected ScrollList(Color color, LinkedList list) {
	this.button = new JButton();
	this.setLayout(new BorderLayout());
	this.setBackground(color);
	this.list       = new JList();
	this.list.setBackground(color);
	this.list.setFixedCellWidth(200);
	this.list.setVisibleRowCount(6);
	//this.list.setDragEnabled(true);
	this.scrollList = new JScrollPane();
	this.scrollList.setBackground(color);
	this.scrollList.setViewportView(this.list);
	this.setList(list);
	this.add(this.scrollList);
    }

    /**
     * Constructs a scroll list with the given list and a  
     * button with the given name.
     */
    protected ScrollList(Color color, 
			 String name) {
	this(color);
	this.button.setText(name);
	this.add(this.button, BorderLayout.SOUTH);
    }

    /**
     * Returns the button used by this scroll list.
     */
    protected JButton getButton() {
	return this.button;
    }
    
    /**
     * Returns the scroll pane for this scroll list.
     */
    protected JScrollPane getScrollList() {
	return this.scrollList;
    }

    /**
     * Returns the list of this scroll list.
     */
    protected JList getList() {
	return this.list;
    }

    /**
     * Returns the selected valus of this list.
     */
    protected Object[] getSelectedValues() {
	return this.list.getSelectedValues();
    }

    /**
     * Returns the selected indices of this list.
     */
    protected int[] getSelectedIndices() {
	return this.list.getSelectedIndices();
    }

    /**
     * Selects the the given indices in this list.
     */
    protected void setSelectedIndices(int[] indices) {
	this.list.setSelectedIndices(indices);
    }

    /**
     * Sets the selection mode of this list.
     */
    protected void setSelectionMode(int mode) {
	this.list.setSelectionMode(mode);
    }

    /*
     * Lists all different string representations of the given list entries.
     */
    protected void setList(LinkedList list) {
	LinkedList scrollList = new LinkedList();

	for (int i=0; i<list.size(); i++) {
	    Object o = list.get(i);
	    String s = ESFC.print((Absynt)o);

	    if (!this.isStringInList(scrollList, s)) 
		scrollList.add(s);
	}
	this.list.setListData(scrollList.toArray());
	this.button.setEnabled(true);
    }

    /*
     * Returns true iff the given list contains the given string.  
     */
    private boolean isStringInList(LinkedList list, String string) {
	for (int i=0; i<list.size(); i++) 
	    if (list.get(i).toString().equals(string))
		return true;
	return false;
    }
    
    /**
     * Sets this list to an empty list.
     */
    protected void removeList() {
	this.setList(new LinkedList());
    }

    /**
     * Disables this list by disabling this button.
     */
    protected void unuseable() {
	this.button.setEnabled(false);
    }
}













