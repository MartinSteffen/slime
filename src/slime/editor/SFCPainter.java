package editor;

import absynt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Hashtable;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: SFCPainter.java,v 1.3 2002-06-06 14:20:39 swprakt Exp $
 */

final class SFCPainter{

    private static final int STEP_HEIGHT = 30;
    private static final Font DATA_FONT =
	new Font( "Courier", Font.PLAIN, 12 );

    private ESFC      eSFC;
    private SFC       sfc;

    protected SFCPainter(ESFC eSFC) {
	this.eSFC = eSFC;
	this.sfc  = eSFC.getSFC();
    }

    protected void paintSFC(Graphics g) {
	g.setFont(DATA_FONT);
	this.paintTransitions(g);
	this.paintSteps(g);
    }

    private void paintSteps(Graphics g) {
	Hashtable colors = this.eSFC.getColorHashtable();
	for (int i=0; i<this.sfc.steps.size(); i++) {
	    Step step = (Step)this.sfc.steps.get(i);
	    g.setColor((Color)colors.get(step));
	    this.paintStep(g, step);
	}
    }

    private void paintStep(Graphics g, Step step) {
	String text = step.name;
	int    x0   = (int)(step.pos.x);
	int    y0   = (int)(step.pos.y);
	int    stepWidth = this.eSFC.getWidth(step);
	g.drawString(text, x0+4, y0+5+STEP_HEIGHT/2);
	g.drawRect(x0, y0, stepWidth-1, STEP_HEIGHT-1);
	if (step == this.sfc.istep)
	    g.drawRect(x0+2, y0+2, stepWidth-5, STEP_HEIGHT-5);
    }

    private void paintTransitions(Graphics g) {
	Hashtable colors = this.eSFC.getColorHashtable();
	LinkedList transitionList = this.sfc.transs;
	for (int i=0; i<transitionList.size(); i++) {
	    Transition transition = (Transition)transitionList.get(i);
	    g.setColor((Color)colors.get(transition));
	    LinkedList source = transition.source;
	    LinkedList target = transition.target;
	    for (int s=0; s<source.size(); s++) {
		Step sStep = (Step)source.get(s);
		Position sPosition = sStep.pos;
		int x0 = (int)sStep.pos.x;
		int y0 = (int)sStep.pos.y;
		for (int t=0; t<target.size(); t++) {
		    Step tStep = (Step)target.get(t);
		    Position tPosition = tStep.pos;
		    int x1 = (int)tStep.pos.x;
		    int y1 = (int)tStep.pos.y;
		    g.setColor(Color.black);
		    if (y0 < y1)
			g.drawLine(x0+15, y0+30, x1+15, y1);
		    else {
			g.drawLine(x0+15, y0+30, x0+35, y0+30);
			g.drawLine(x0+35, y0+30, x1+35, y1);
			g.drawLine(x1+15, y1, x1+35, y1);
		    }
		}
	    }
	}
    }
}









