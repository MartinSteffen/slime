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
 * @version $Id: ESFC.java,v 1.1 2002-05-30 13:03:52 swprakt Exp $
 */

final class ESFC{

    private static final Color INITIAL = Color.darkGray;
    private static final Color SOURCE  = Color.green;
    private static final Color TARGET  = Color.red;
    private static final Color NORMAL  = Color.black;

    private SFC        sfc;
    private LinkedList sourceSteps;
    private LinkedList targetSteps;
    private Hashtable  colors;
    private DrawBoard  drawBoard;
    
    protected ESFC(SFC sfc) {
	this.sfc    = sfc;
	this.colors = this.createColorHashtable();
	this.sourceSteps = new LinkedList();
	this.targetSteps = new LinkedList();
    }

    protected SFC getSFC() {
	return this.sfc;
    }

    protected void setDrawBoard(DrawBoard drawBoard) {
	this.drawBoard = drawBoard;
    }

    protected DrawBoard getDrawBoard() {
	return this.drawBoard;
    }

    protected LinkedList getSourceSteps() {
	return this.sourceSteps;
    }

    protected LinkedList getTargetSteps() {
	return this.targetSteps;
    }

    protected int getNumberOfSourceSteps() {
	return this.sourceSteps.size();
    }

    protected int getNumberOfTargetSteps() {
	return this.targetSteps.size();
    }

    protected void clearSourceSteps() {
	for (int i=0; i<this.sourceSteps.size(); i++) {
	    Step step = (Step)this.sourceSteps.get(i);
	    this.colors.put(step, NORMAL);
	}
	this.sourceSteps = new LinkedList();
    }

    protected void clearTargetSteps() {
	for (int i=0; i<this.targetSteps.size(); i++) {
	    Step step = (Step)this.targetSteps.get(i);
	    this.colors.put(step, NORMAL);
	}
	this.targetSteps = new LinkedList();
    }

    protected void removeStep(Step step) {
	this.removeSourceStep(step);
	this.removeTargetStep(step);
  	LinkedList transitionList = this.sfc.transs;
  	for (int i=0; i<transitionList.size(); i++) {
  	    Transition transition = (Transition)transitionList.get(i);
  	    LinkedList source = transition.source;
  	    LinkedList target = transition.target;
  	    for (int s=0; s<source.size(); s++) {
  		Step sStep = (Step)source.get(s);
  		if (sStep == step)
  		    source.remove(s--);
  	    }
  	    for (int t=0; t<target.size(); t++) {
  		Step tStep = (Step)target.get(t);
  		if (tStep == step)
  		    target.remove(t--);
  	    }
  	    if ((source.size() == 0) || (target.size() == 0))
  		transitionList.remove(i--);
  	}
  	this.sfc.steps.remove(step);
	this.colors.remove(step);
    }

    protected void removeSourceStep(Step step) {
	this.sourceSteps.remove(step);
	this.colors.put(step, NORMAL);
    }

    protected void removeTargetStep(Step step) {
	this.targetSteps.remove(step);
	this.colors.put(step, NORMAL);
    }

    protected void addSourceStep(Step step) {
	this.sourceSteps.add(step);
	this.colors.put(step, SOURCE);
    }

    protected void addTargetStep(Step step) {
	this.targetSteps.add(step);
	this.colors.put(step, TARGET);
    }

    protected boolean isSourceStep(Step step) {
	return (this.sourceSteps.indexOf(step) != -1);
    }

    protected boolean isTargetStep(Step step) {
	return (this.targetSteps.indexOf(step) != -1);
    }
    
    private Hashtable createColorHashtable() {
	Hashtable colors = new Hashtable();
	for (int nr=0; nr<this.sfc.steps.size(); nr++) { 
	    Step step = (Step)(this.sfc.steps).get(nr);
	    if (step == this.sfc.istep)
		colors.put(step, INITIAL);
	    else
		colors.put(step, NORMAL);
	}
	for (int nr=0; nr<sfc.transs.size(); nr++) { 
	    Transition transition = (Transition)(this.sfc.transs).get(nr);
	    colors.put(transition, Color.black);
	}
	return colors;
    }

    protected void addStep(String name, int x, int y) {
	Step step = new Step(name);
	Position position = new Position((float)x, (float)y);
	step.pos = position;
	this.sfc.steps.add(step);
	colors.put(step, NORMAL);
    }

    protected void addTransition(LinkedList source, Expr expr, LinkedList target) {
	Transition transition = new Transition(source, expr, target);
	this.sfc.transs.add(transition);
	colors.put(transition, NORMAL);
	this.clearSourceSteps();
	this.clearTargetSteps();
    }

    protected Hashtable getColorHashtable() {
	return this.colors;
    }

    protected void highlight(Object key, Color color) {
	if (this.colors.containsKey(key))
	    this.colors.put(key, color);
    }

    protected void deHighlight(Object key) {
	this.highlight(key, NORMAL);
    }
}









