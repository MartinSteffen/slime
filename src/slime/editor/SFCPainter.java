package slime.editor;

import slime.absynt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Hashtable;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: SFCPainter.java,v 1.8 2002-06-12 18:52:04 swprakt Exp $
 */

final class SFCPainter{

    private static final int STEP_HEIGHT = 30;
    protected static final int ACTION_GAP = 10;

    private ESFC      eSFC;
    private SFC       sfc;

    protected SFCPainter(ESFC eSFC) {
	this.eSFC = eSFC;
	this.sfc  = eSFC.getSFC();
    }

    protected void paintSFC(Graphics g) {
	g.setFont(this.eSFC.getFont());
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
	Object selectedObject = this.eSFC.getSelectedObject();
	g.drawString(text, x0+4, y0+6+STEP_HEIGHT/2);
	g.drawRect(x0, y0, stepWidth-1, STEP_HEIGHT-1);
	if (step == this.sfc.istep)
	    g.drawRect(x0+2, y0+2, stepWidth-5, STEP_HEIGHT-5);
	if (step == selectedObject) {
	    g.setColor(Color.yellow);
	    g.drawRect(x0-1, y0-1, stepWidth+1, STEP_HEIGHT+1);
	}
	LinkedList actions = step.actions;
	if (actions.size() != 0) {
	    g.setColor(Color.gray);
	    g.drawLine(x0+stepWidth, y0+7, x0+stepWidth+ACTION_GAP, y0+7);
	    for (int i=0; i< actions.size(); i++) {
		StepAction stepAction = (StepAction)actions.get(i);
		String name = stepAction.a_name;
		g.drawString(name, x0+4+ACTION_GAP+stepWidth, y0+11+i*15);
		//FIX ME: *7 muss noch weg!
		g.drawRect(x0+ACTION_GAP+stepWidth, y0+i*15, name.length()*7 + 8, 15);
	    }
	}
    }

    private void paintTransitions(Graphics g) {
	Hashtable colors = this.eSFC.getColorHashtable();
	LinkedList transitionList = this.sfc.transs;
	int gap = 20;
	for (int i=0; i<transitionList.size(); i++) {
	    Transition transition = (Transition)transitionList.get(i);
	    LinkedList source = transition.source;
	    LinkedList target = transition.target;
	    boolean openClosure = target.size() > 1;
	    boolean closingClosure = source.size() > 1;
	    String name = this.eSFC.output(transition.guard);
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
		    g.setColor((Color)colors.get(transition));
		    if (y0 < y1) {
			int yOffset = (y1-y0-30)/2;
			g.drawLine(x0+15, y0+30, x0+15, y0+30+yOffset);
			g.drawLine(x0+15, y0+30+yOffset, x1+15, y0+30+yOffset);
			g.drawLine(x1+15, y0+30+yOffset, x1+15, y1);
		    } else {
			int x0Offset = -40;
			int x1Offset = -40-(x1-x0);
			if (x1 < x0) {
			    x0Offset = -40-(x0-x1);
			    x1Offset = -40;
			}
			g.drawLine(x0+15, y0+30, x0+15, y0+30+gap);
			g.drawLine(x0+15, y0+30+gap, x0+15+x0Offset, y0+30+gap);
			g.drawLine(x0+15+x0Offset, y0+30+gap, x1+15+x1Offset, y1-gap);
			g.drawLine(x1+15+x1Offset, y1-gap, x1+15, y1-gap);
			g.drawLine(x1+15, y1-gap, x1+15, y1);
		    }
		    if (openClosure) {
			g.drawLine(x0+10, y0+30+gap/2, x0+20, y0+30+gap/2);
			g.setColor(Color.gray);
			g.drawString(name, x0+23, y0+30+gap/2+2);
		    } else {
			g.drawLine(x1+10, y1-gap/2, x1+20, y1-gap/2);
			g.setColor(Color.gray);
			g.drawString(name, x1+23, y1-gap/2+2);
		    }
		}
	    }
	}
    }
}









