package editor;

import absynt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: DrawBoard.java,v 1.8 2002-06-12 08:39:34 swprakt Exp $
 */

final class DrawBoard extends Canvas
    implements MouseListener, MouseMotionListener, KeyListener {

    private static final int START_WIDTH = 640, START_HEIGHT = 480;

    private int     mousePressedOnX, mousePressedOnY;
    private int     mouseReleasedOnX, mouseReleasedOnY;
    private boolean mouseDragged;
    private boolean mouseMoved;
    private Editor  editor;
    private ESFC    eSFC;

    protected DrawBoard(Editor editor) {
	this.editor = editor;
	this.eSFC = editor.getExtendedSelectedSFC();
	this.initWindow();
    }

    private void initWindow() {
	this.addMouseListener(this);
	this.addMouseMotionListener(this);
	this.addKeyListener(this);
	this.setSize(START_WIDTH, START_HEIGHT);
	this.setBackground(Color.white);
	this.setVisible(true);
    }

    protected ESFC getESFC() {
	return this.eSFC;
    }

    private void updateSize() {
	SFC sfc = this.eSFC.getSFC();
	int maxX = Integer.MIN_VALUE;
	int minX = Integer.MAX_VALUE;
	int maxY = Integer.MIN_VALUE;
	int minY = Integer.MAX_VALUE;
	for (int i=0; i<sfc.steps.size(); i++) {
	    Step step = (Step)sfc.steps.get(i);
	    Position position = step.pos;
	    int x = (int)position.x;
	    int y = (int)position.y;
	    int stepWidth = this.eSFC.getWidth(step) 
		+ this.eSFC.getWidthOfActionList(step.actions);
	    if ((x + stepWidth) > maxX)
		maxX = x + stepWidth;
	    if (x < minX)
		minX = x;
	    if (y > maxY)
		maxY = y;
	    if (y < minY)
		minY = y;
	}
	maxX += 30;
	maxY += 30;
	minX -= 30;
	minY -= 30;
	maxX -= minX;
	maxY -= minY;
	if (maxX < 640)
	    maxX = 640;
	if (maxY < 450)
	    maxY = 450;
	for (int i=0; i<sfc.steps.size(); i++) {
	    Step step = (Step)sfc.steps.get(i);
	    Position position = step.pos;
	    position.x -= (float)minX;
	    position.y -= (float)minY;
	}
	this.setSize(maxX, maxY+32);
    }
    
    public void mouseClicked(MouseEvent e) {
	e.consume();
	int amountOfClicks = e.getClickCount();
	if (amountOfClicks == 1)
	    this.editor.evaluateSingleMouseClickOn(this.mousePressedOnX, 
						   this.mousePressedOnY);
	else if (amountOfClicks == 2)
	    this.editor.evaluateDoubleMouseClickOn(this.mousePressedOnX, 
						   this.mousePressedOnY);
	this.repaint();
    }

    public void mouseEntered(MouseEvent e) {
	this.requestFocus();
	e.consume();
	this.repaint();
    }

    public void mouseExited(MouseEvent e) {
	e.consume();
	this.repaint();
    }

    public void mousePressed(MouseEvent e) {
	e.consume();
	this.mousePressedOnX = e.getX();
	this.mousePressedOnY = e.getY();
	this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
	e.consume();
	this.mouseReleasedOnX = e.getX();
	this.mouseReleasedOnY = e.getY();
	if (this.mouseDragged)
	    this.editor.evaluateMouseDragged(this.mousePressedOnX,
					     this.mousePressedOnY,
					     this.mouseReleasedOnX,
					     this.mouseReleasedOnY);
	this.mouseDragged = false;
	this.repaint();
    }

    public void mouseDragged(MouseEvent e) {
	e.consume();
	this.mouseDragged = true;
    }

    public void mouseMoved(MouseEvent e) {
	e.consume();
	if (this.editor.evaluateMouseMoved(e.getX(), 
					   e.getY()))	
	    this.repaint();
    }

    public void keyTyped(KeyEvent e) {
	e.consume();
	this.editor.evaluateKeyTyped(e.getKeyChar());
	this.repaint();
    }

    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }

    public void paint(Graphics g) {
	super.paint(g);
	this.updateSize();
	SFCPainter sfcPainter = new SFCPainter(this.eSFC);
	sfcPainter.paintSFC(g);
    }
}




















