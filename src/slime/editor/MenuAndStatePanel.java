package slime.editor;

import slime.absynt.*;
import slime.layout.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.*;
import java.util.zip.*;

/**
 * An object of this class represents a part of the editor display.
 * It contains a status message line and several buttons for operating
 * on a sfc.
 * <br><br>
 * Status: nearly complete <br>
 * known bugs: - (But some expected) <br>
 * @author Andreas Niemann
 * @version $Id: MenuAndStatePanel.java,v 1.3 2002-06-14 11:21:03 swprakt Exp $
 */

public final class MenuAndStatePanel extends JPanel {

    private Editor     editor;
    private JButton    checkButton;    
    private JButton    layoutButton;
    private JButton    simulateButton;
    private JButton    guardButton; 
    private JButton    actionButton;
    private JButton    variableButton;
    private JTextField statusMessage;

    /**
     * Constructs a JPanel for an editor with several 
     * buttons and a status message line.
     * @param editor The editor to which this Panel should belong to.
     */
    protected MenuAndStatePanel(Editor editor) {
	this.editor = editor;

	this.setLayout(new BorderLayout());

	this.statusMessage = new JTextField( 40 );
	this.add(statusMessage, BorderLayout.CENTER);

	JPanel south = new JPanel();
	this.addGuardButton(south);
	this.addActionButton(south);
	this.addVariableButton(south);
	JLabel label = new JLabel("-= SFC-Editor =-");
	label.setFont(new Font( "Courier", Font.PLAIN, 20 ));
	label.setForeground(Color.blue);
	south.add(label);
	this.addCheckButton(south);
	this.addLayoutButton(south);
	this.addSimulateButton(south);
	south.setBackground(Editor.BACKGROUND_COLOR);
	this.add(south, BorderLayout.SOUTH);

	this.setBorder(this.editor.getTitledBorder("Menu & Status"));
	this.setBackground(Editor.BACKGROUND_COLOR);
    }

    /**
     * Writes the given message into the status message line. 
     */
    protected void setStatusMessage(String message) {
	this.statusMessage.setText(message);
    }

    /**
     * A call of this method checks the current selected sfc in the editor
     * and enables buttons depending on the result of the check.
     */
    protected void enableButtons() {
	ESFC eSFC = editor.getExtendedSelectedSFC();
	this.guardButton.setEnabled(eSFC != null);
	this.actionButton.setEnabled(eSFC != null);
	this.variableButton.setEnabled(eSFC != null);
	this.checkButton.setEnabled(eSFC != null);
	boolean checked;
	boolean layouted;
	if (eSFC == null) { 
	    checked = false;
	    layouted = false;
	} else {
	    checked = eSFC.isChecked();
	    layouted = eSFC.isLayouted();
	}
	this.layoutButton.setEnabled(checked && !layouted);
	this.simulateButton.setEnabled(checked);
    }

    private void addSimulateButton(JPanel panel) {
	this.simulateButton = new JButton("Simulate");
	this.simulateButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Simulator not yet implemented.");
		}
	    });
	panel.add(this.simulateButton);
    }

    private void addLayoutButton(JPanel panel) {
	this.layoutButton = new JButton("Layout");
	this.layoutButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Layouter trys his best at his actual implementation ...");
		    ESFC eSFC = editor.getExtendedSelectedSFC();
		    Layouter.position_sfc(eSFC.getSFC());
		    eSFC.getDrawBoard().repaint();
		    eSFC.setLayouted(true);
		    enableButtons();
		}
	    });
	panel.add(this.layoutButton);
    }

    private void addCheckButton(JPanel panel) {
	this.checkButton = new JButton("Check");
	this.checkButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Checker not yet implemented.");
		    editor.getExtendedSelectedSFC().setChecked(true);
		    enableButtons();
		}
	    });
 	panel.add(this.checkButton);
    }

    private void addVariableButton(JPanel panel) {
	this.variableButton = new JButton("Variable");
	this.variableButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new variable.");
		}
	    });
	panel.add(this.variableButton);
    }

    private void addActionButton(JPanel panel) {
	this.actionButton = new JButton("Action");
	this.actionButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new action.");
		    String fN = "snapshot.sfc";
		    try {
			FileInputStream fOS = new FileInputStream(fN);
			GZIPInputStream gZOS = new GZIPInputStream(fOS);
			ObjectInputStream oOS = new ObjectInputStream(gZOS);
			Object o = oOS.readObject();
			editor.add((SFC)o);
			oOS.close();
		    } catch (IOException ioe)
		    {System.out.println("Fehler beim Laden");
		    } catch (ClassNotFoundException cnfe)
		    {System.out.println("Fehler beim Laden");}
		}
	    });
	panel.add(this.actionButton);
    }

    private void addGuardButton(JPanel panel) {
	this.guardButton = new JButton("Guard");
	this.guardButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Add new guard.");
		    String fN = "snapshot.sfc";
		    try {
			FileOutputStream fOS = new FileOutputStream(fN);
			GZIPOutputStream gZOS = new GZIPOutputStream(fOS);
			ObjectOutputStream oOS = new ObjectOutputStream(gZOS);
			SFC sfc = editor.getSelectedSFC();
			oOS.writeObject(sfc);
			oOS.flush();
			oOS.close();
		    } catch (IOException ioe)
		    {System.out.println("Fehler beim Speichern");}
		}
	    });
	panel.add(this.guardButton);
    }
}    
    
    
    

    








