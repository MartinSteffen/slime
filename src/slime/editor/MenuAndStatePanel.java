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

import java.io.File;

/**
 * An object of this class represents a part of the editor display.
 * It contains a status message line and several buttons for operating
 * on a sfc.
 * <br><br>
 * Status: nearly complete <br>
 * known bugs: - (But some expected) <br>
 * @author Andreas Niemann
 * @version $Id: MenuAndStatePanel.java,v 1.4 2002-06-20 11:25:06 swprakt Exp $
 */

public final class MenuAndStatePanel extends JPanel {

    private Editor     editor;
    private JLabel     mode;
    private JButton    checkButton;    
    private JButton    layoutButton;
    private JButton    simulateButton;
    private JButton    loadButton; 
    private JButton    saveButton;
    private JButton    closeButton;
    private JButton    exitButton;
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
	JPanel file = new JPanel();
	this.addLoadButton(file);
	this.addSaveButton(file);
	this.addCloseButton(file);
	file.setBackground(Editor.BACKGROUND_COLOR);
	file.setBorder(editor.getTitledBorder("File"));
	south.add(file);
	JPanel operation = new JPanel(); 
	this.addCheckButton(operation);
	this.addLayoutButton(operation);
	this.addSimulateButton(operation);
	operation.setBackground(Editor.BACKGROUND_COLOR);
	operation.setBorder(editor.getTitledBorder("Operations"));
	south.add(operation);
	this.mode = new JLabel(); 
	this.mode.setFont(new Font( "Courier", Font.PLAIN, 30 ));
	this.mode.setBorder(editor.getTitledBorder("Mode"));
	south.add(mode);
	JLabel label = new JLabel("-= SFC-Editor =-");
	label.setFont(new Font( "Courier", Font.PLAIN, 30 ));
	label.setBorder(editor.getTitledBorder("What am i"));
	label.setForeground(new Color(150, 0, 150));
	south.add(label);
	JPanel exit = new JPanel();
	this.addExitButton(exit);
	exit.setBackground(Editor.BACKGROUND_COLOR);
	exit.setBorder(editor.getTitledBorder("Quit"));
	south.add(exit);
	south.setBackground(Editor.BACKGROUND_COLOR);
	this.add(south, BorderLayout.SOUTH);

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
	this.loadButton.setEnabled(true);
	this.saveButton.setEnabled(eSFC != null);
	this.closeButton.setEnabled(eSFC != null);
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
	this.exitButton.setEnabled(true);
    }

    protected void setMode(String mode) {
	this.mode.setText(" "+mode+" ");
    }

    private JButton getSmallGapButton(String name) {
	JButton button = new JButton(name);
	button.setMargin(new Insets(2, 2, 1, 1));
	return button;
    }

    private void addExitButton(JPanel panel) {
	this.exitButton = this.getSmallGapButton("Exit");
	this.exitButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Leaving SFC-Editor ...");
		    Object[] message = new Object[1];
		    message[0] = "Do you really want to exit?";
		    String[] options = {"Ok", "Cancel"};
		    int result = JOptionPane.showOptionDialog(
			null,
			message,
			"Everything saved?",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);
		    if (result == 0) 
			System.exit(0);
		}
	    });
	panel.add(this.exitButton);
    }

    private void addSimulateButton(JPanel panel) {
	this.simulateButton = this.getSmallGapButton("Simulate");
	this.simulateButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Simulator not yet implemented.");
		}
	    });
	panel.add(this.simulateButton);
    }

    private void addLayoutButton(JPanel panel) {
	this.layoutButton = this.getSmallGapButton("Layout");
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
	this.checkButton = this.getSmallGapButton("Check");
	this.checkButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Checker not yet implemented.");
		    ESFC eSFC = editor.getExtendedSelectedSFC();
		    eSFC.setChecked(true);
		    if (eSFC.getSFC().istep == null) {
			statusMessage.setText("Check fails, there is no initial step in this SFC.");
			eSFC.setChecked(false);
		    }
		    enableButtons();
		}
	    });
 	panel.add(this.checkButton);
    }

    private void addCloseButton(JPanel panel) {
	this.closeButton = this.getSmallGapButton("Close");
	this.closeButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Closing SFC ...");
		    Object[] message = new Object[1];
		    message[0] = "Do you really want to close it?";
		    String[] options = {"Ok", "Cancel"};
		    int result = JOptionPane.showOptionDialog(
			null,
			message,
			"Possibly not saved!",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);
		    if (result == 0) {
			editor.closeSelectedSFC();
			statusMessage.setText("SFC closed.");
		    }
		}
	    });
	panel.add(this.closeButton);
    }

    private void addLoadButton(JPanel panel) {
	this.loadButton = this.getSmallGapButton("Load");
	this.loadButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Loading SFC ...");
		    File f = getFileName(false);
		    if (f == null)
			return;
		    String fN = f.getAbsolutePath();
		    if (fN == null) return;
		    try {
			FileInputStream fOS = new FileInputStream(fN);
			GZIPInputStream gZOS = new GZIPInputStream(fOS);
			ObjectInputStream oOS = new ObjectInputStream(gZOS);
			Object o = oOS.readObject();
			editor.add((SFC)o, f.getName());
			oOS.close();
			statusMessage.setText("SFC loaded.");
		    } catch (IOException ioe)
		    {System.out.println("Fehler beim Laden.");
		    } catch (ClassNotFoundException cnfe)
		    {System.out.println("Fehler beim Laden.");}
		}
	    });
	panel.add(this.loadButton);
    }

    private void addSaveButton(JPanel panel) {
	this.saveButton = this.getSmallGapButton("Save");
	this.saveButton.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    statusMessage.setText("Saving SFC ...");
		    File f = getFileName(true);
		    if (f == null) return;
		    String fN = f.getAbsolutePath();
		    if(fN == null) return;
		    int result = 0;
		    if (f.exists()) {
			Object[] message = new Object[1];
			message[0] = "Do you really want to overwrite it?";
			String[] options = {"Ok", "Cancel"};
			result = JOptionPane.showOptionDialog(
			    null,
			    message,
			    "File already exists!",
			    JOptionPane.DEFAULT_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[0]);
		    }
		    if (result == 0) {
			try {
			    FileOutputStream fOS = new FileOutputStream(fN);
			    GZIPOutputStream gZOS = new GZIPOutputStream(fOS);
			    ObjectOutputStream oOS = new ObjectOutputStream(gZOS);
			    SFC sfc = editor.getSelectedSFC();
			    oOS.writeObject(sfc);
			    oOS.flush();
			    oOS.close();
			    fN = f.getName();
			    int i = fN.lastIndexOf('.');
			    fN = fN.substring(0,i);
			    sfc.name = fN;
			    statusMessage.setText("SFC saved.");
			} catch (IOException ioe)
			{System.out.println("Fehler beim Speichern");}
		    }
		}
	    });
	panel.add(this.saveButton);
    }
    
    private File getFileName(boolean save) { 
	JFileChooser chooser = new JFileChooser();
	SFCFileFilter filter = new SFCFileFilter();
	chooser.setFileFilter(filter);     
	int returnVal;
	JFrame parent = new JFrame();
	if (save)
	    returnVal = chooser.showSaveDialog(parent);
	else
	    returnVal = chooser.showOpenDialog(parent);
	if(returnVal == JFileChooser.APPROVE_OPTION) 
	    return chooser.getSelectedFile();
	return null;
    }
}    

class SFCFileFilter extends javax.swing.filechooser.FileFilter {

    public boolean accept(File f) {
	if(f != null) {
	    if(f.isDirectory())
		return true;
	    String extension = getExtension(f);
	    if(extension != null && extension.equals("sfc")) {
		return true;
	    };
	}
	return false;
    }

    public String getExtension(File f) {
	if(f != null) {
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if(i>0 && i<filename.length()-1)
		return filename.substring(i+1).toLowerCase();
	}
	return null;
    }

    public String getDescription() {
	return "SFC Projects";
    }
}

    
    

    








