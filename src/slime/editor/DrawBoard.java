package slime.editor;

import slime.absynt.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.*;

/**
 * An object of this class is used to draw a sfc on it.
 * <br><br>
 * Status: nearly complete, only some docs are missing <br>
 * Known bugs: - <br>
 * @author Andreas Niemann
 * @version $Id: DrawBoard.java,v 1.14 2002-07-09 23:37:23 swprakt Exp $
 */

public final class DrawBoard extends Canvas
    implements MouseListener, MouseMotionListener, KeyListener {

    private static final int START_WIDTH = 700, START_HEIGHT = 480;

    private int     mousePressedOnX, mousePressedOnY;
    private int     mouseReleasedOnX, mouseReleasedOnY;
    private boolean mouseDragged;

    private Editor  editor;
    private ESFC    eSFC;
    private int     maxX, minX, maxY, minY;

    protected DrawBoard(Editor editor, ESFC eSFC) {
	this.editor = editor;
	this.eSFC   = eSFC;
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

    private void updateSize() {
	this.maxX = Integer.MIN_VALUE;
	this.minX = Integer.MAX_VALUE;
	this.maxY = Integer.MIN_VALUE;
	this.minY = Integer.MAX_VALUE;

	eSFC.forAllSteps(this, "determineDimension", null);

	this.adjustDimension();

	Object[] args = new Object[2];
	args[0] = new Float((float)minX);
	args[1] = new Float((float)minY);
	eSFC.forAllPositions(this, "translatePosition" , args);

	this.setSize(maxX, maxY+32);
    }
    
    protected void translatePosition(Position position, float x, float y) {
	position.x -= x;
	position.y -= y;
    }

    protected void determineDimension(Step step) {
	Position position  = step.pos;
	// FIX-ME: Checker?
	if (position == null) {
	    position = new Position(0.0f, 0.0f);
	    step.pos = position;
	}
	int      x         = (int)position.x;
	int      y         = (int)position.y;
	int      stepWidth = this.eSFC.getWidth(step) 
	    + this.eSFC.getWidthOfActionList(step.actions);
	
	if ((x + stepWidth) > this.maxX) this.maxX = x + stepWidth;
	if (x < this.minX) this.minX = x;
	if (y > this.maxY) this.maxY = y;
	if (y < this.minY) this.minY = y;
    }

    protected void adjustDimension() {
	this.maxX += 30; this.maxY += 30;
	this.minX -= 30; this.minY -= 30;
	this.maxX -= this.minX; this.maxY -= minY;
	if (this.maxX < 700) this.maxX = 700;
	if (this.maxY < 450) this.maxY = 450;
    }

    public void mouseClicked(MouseEvent e) {
	e.consume();
	this.editor.evaluateSingleMouseClickOn(this.mousePressedOnX, 
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

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    /**
     * This method updates the size of this DrawBoard 
     * and paints the sfc with a SFCPainter.
     */
    public void paint(Graphics g) {
	super.paint(g);
	this.updateSize();
	(new SFCPainter(this.eSFC)).paintSFC(g);
    }
}




















