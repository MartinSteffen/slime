package slime.editor;

import slime.absynt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Hashtable;

/**
 * An object of this class is used to paint a sfc on an Graphics object.
 * <br><br>
 * Status: about 80% complete <br>
 * Known bugs: parallel branches are not shown correctly, some more trouble with steps with more than one transition 
 * @author Andreas Niemann
 * @version $Id: SFCPainter.java,v 1.13 2002-07-09 23:37:24 swprakt Exp $
 */

public final class SFCPainter{

    private   static final int STEP_HEIGHT = 30;
    protected static final int ACTION_GAP  = 10;

    private ESFC      eSFC;
    private SFC       sfc;
    private int[]     incoming;
    private int[]     outgoing;

    /**
     * Constructs a painter for the given ESFC.
     */
    protected SFCPainter(ESFC eSFC) {
	this.eSFC = eSFC;
	this.sfc  = eSFC.getSFC();
    }

    /**
     * Paints the sfc on the given Graphics object.
     */
    protected void paintSFC(Graphics g) {
	g.setFont(this.eSFC.getFont());
	this.calculateIncomingAndOutgoingTransitions();
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
	
	if (step == selectedObject)
	    g.setColor(Color.magenta);
	g.fillRect(x0, y0, stepWidth-1, STEP_HEIGHT-1);
	g.setColor(Color.black);
	g.drawRect(x0, y0, stepWidth-1, STEP_HEIGHT-1);
	g.drawString(text, x0+5, y0+6+STEP_HEIGHT/2);
	if (step == this.sfc.istep)
	    g.drawRect(x0+2, y0+2, stepWidth-5, STEP_HEIGHT-5);
	LinkedList actions = step.actions;
	if (actions.size() != 0) {
	    g.setColor(Color.gray);
	    g.drawLine(x0+stepWidth, y0+7, x0+stepWidth+ACTION_GAP, y0+7);
	    for (int i=0; i< actions.size(); i++) {
		StepAction stepAction = (StepAction)actions.get(i);
		String name = stepAction.a_name;
		String qualifier = this.eSFC.output(stepAction.qualifier);
		int qual = 12;
		
		slime.absynt.Action action = this.eSFC.getAction(name);
		if (stepAction == selectedObject)
		    g.setColor(Color.magenta);
		else {
		    Hashtable colors = this.eSFC.getColorHashtable();
		    Color color = (Color)colors.get(action);
		    g.setColor(color);
		}
		g.fillRect(x0+ACTION_GAP+stepWidth, 
			   y0+i*15, name.length()*7 + 8+ qual, 15);
		g.setColor(Color.gray);
		
		g.drawString(qualifier, x0+2+ACTION_GAP+stepWidth, y0+11+i*15);
		g.drawString(name, x0+4+ACTION_GAP+stepWidth+qual, y0+11+i*15);
		//FIX ME: *7 muss noch weg!
		g.drawRect(x0+ACTION_GAP+stepWidth, 
			   y0+i*15, name.length()*7 + 8+ qual, 15);
		g.drawLine(x0+ACTION_GAP+stepWidth+qual,
			   y0+i*15,
			   x0+ACTION_GAP+stepWidth+qual,
			   y0+(i+1)*15);
	    }
	}
    }

    private void calculateIncomingAndOutgoingTransitions() {
	incoming = new int[this.sfc.steps.size()];
	outgoing = new int[this.sfc.steps.size()];
	LinkedList transitions = this.sfc.transs;
	for (int i=0; i<transitions.size(); i++) {
	    Transition transition = (Transition)transitions.get(i);
	    LinkedList sources = transition.source;

	    for (int j=0; j<sources.size(); j++) {
		outgoing[this.getStepNumber((Step)sources.get(j))] += 1;
	    }
	    LinkedList targets = transition.target;

	    for (int j=0; j<targets.size(); j++) {
		incoming[this.getStepNumber((Step)targets.get(j))] += 1;
	    }
	}
    }

    private int getStepNumber(Step step) {
	LinkedList steps = this.sfc.steps;
	for (int i=0; i<steps.size(); i++) 
	    if (step == (Step)steps.get(i))
		return i;
	return -1;
    }

    private void paintTransitions(Graphics g) {
	// FIX-ME: ausgewaehlte Transitionen am Schluss malen, sonst
	// sieht man sie eventuell nicht.
	Object selectedObject = this.eSFC.getSelectedObject();
	Hashtable colors = this.eSFC.getColorHashtable();
	Hashtable guardPositions = new Hashtable();
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

		    if (transition == selectedObject)
			g.setColor(Color.magenta);
		    else
			g.setColor((Color)colors.get(transition));
		    
		    if (y0 < y1) {
			int yOffset = 20; //(y1-y0-30)/2;
			if (closingClosure || (incoming[this.getStepNumber(tStep)] > 1))
			    yOffset = (y1-y0-50);
			g.drawLine(x0+15, y0+30, x0+15, y0+30+yOffset);
			g.drawLine(x0+15, y0+30+yOffset, x1+15, y0+30+yOffset);
			g.drawLine(x1+15, y0+30+yOffset, x1+15, y1);
			if (openClosure)
			    g.drawLine(x0+15, y0+33+yOffset, x1+15, y0+33+yOffset);
			if (closingClosure)
			    g.drawLine(x0+15, y0+27+yOffset, x1+15, y0+27+yOffset);
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
		    if (openClosure || (incoming[this.getStepNumber(tStep)] > 1)) {
			g.drawLine(x0+10, y0+30+gap/2, x0+20, y0+30+gap/2);
			g.setColor(Color.gray);
			g.drawString(name, x0+23, y0+30+gap/2+2);
			guardPositions.put(transition, 
					   new Position((float)(x0+10), 
							(float)(y0+30+gap/2)));
		    } else if (closingClosure || (outgoing[this.getStepNumber(sStep)] > 1)){
			g.drawLine(x1+10, y1-gap/2, x1+20, y1-gap/2);
			g.setColor(Color.gray);
			g.drawString(name, x1+23, y1-gap/2+2);
			guardPositions.put(transition, 
					   new Position((float)(x1+10), 
							(float)(y1-gap/2)));
		    } else {
			g.drawLine(x0+10, y0+30+gap/2, x0+20, y0+30+gap/2);
			g.setColor(Color.gray);
			g.drawString(name, x0+23, y0+30+gap/2+2);
			guardPositions.put(transition, 
					   new Position((float)(x0+10), 
							(float)(y0+30+gap/2)));
		    } 
		}
	    }
	}
	this.eSFC.setGuardPositions(guardPositions);
    }
}









