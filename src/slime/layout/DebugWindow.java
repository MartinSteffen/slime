package layout;

import absynt.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: DebugWindow.java,v 1.4 2002-05-30 13:04:37 swprakt Exp $
 */

class DebugWindow extends JFrame {

    private static final boolean DEBUG = true;
    private static final int WIDTH = 760, HEIGHT = 600;

    private JTextArea textArea;
    private Layouter  layouter;

    protected DebugWindow(Layouter layouter) {
	this.layouter = layouter;
	this.initWindow("SFC-Layouter");
    }

    private void initWindow(String title) {
	this.setSize(WIDTH, HEIGHT);
	this.setBackground(Color.gray);
	this.setTitle(title);
	this.getContentPane().add(this.getPanel(), "South");
	this.setVisible(DEBUG);
    }

    private Panel getPanel() {
	Panel panel = new Panel();
	panel.add(this.getJScrollPane());
	panel.add(this.getCloseButton());
	return panel;
    }

    private JScrollPane getJScrollPane() {
	this.textArea = new JTextArea(10, 40);
	return new JScrollPane(textArea); 
    }

    private Button getCloseButton() {
	Button close = new Button("close");
	close.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent e) {
		    setVisible(false); //System.exit(0);
		}
	    });
	return close;
    }

    protected void write(String text) {
	if (DEBUG)
	    this.textArea.append(text);
    }

    protected void write(int number) {
	this.write(number+"");
    }

    protected void writeln(String text) {
	this.write(text+"\n");
    }

    protected void writeln(int number) {
	this.write(number+"\n");
    }

    protected void writeln() {
	this.write("\n");
    }

    public void paint(Graphics g) {
	layouter.paint(g);
    }

    public static void main(String[] argv) {
	SFC sfc1 = Example.getExample1();
	Layouter.position_sfc(sfc1);
    }
}









