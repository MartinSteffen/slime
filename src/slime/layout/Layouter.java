package slime.layout;

import slime.absynt.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;
import javax.swing.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: Layouter.java,v 1.10 2002-06-14 11:21:05 swprakt Exp $
 */

public final class Layouter {

    private static final Integer NO_STEP     = new Integer(-1);
    private static final int     STEP_HEIGHT = 30;
    private static final int     STEP_WIDTH  = 30;
    private static final boolean TOP = false, BOTTOM = true; 

    private StepList[]   graph;
    private SFC          sfc;
    private Hashtable    hashtable;

    private DebugWindow  debug;

    private Layouter(SFC sfc) {

	this.debug = new DebugWindow(this);

	this.sfc = sfc;
	this.createHashtable();

	this.debug.writeln("Hashtable: "+this.hashtable);
	this.debug.writeln("iStep: "+sfc.istep.name);
	this.debug.writeln("#steps: "+sfc.steps.size());
    }

    /**
     * This method applies a graphical layout algorithm on the given sfc. 
     * @param sfc a checked SFC.
     * @return this sfc positioned.
     */
    public static SFC position_sfc(SFC sfc) {
        return (new Layouter(sfc)).placeInLevelOrder();
    }

    /**
     * Creates this hashtable with the Steps as keys.
     */
    private void createHashtable() {
	this.debug.writeln("Creating hashtable with steps as keys ...");
	this.hashtable = new Hashtable();
	for (int nr=0; nr<this.sfc.steps.size(); nr++) { 
	    Step step = (Step)(this.sfc.steps).get(nr);
	    hashtable.put(step, new Integer(nr));
	}
    }

    private Integer getStepNumber(Step step) {
	return (Integer)this.hashtable.get(step);
    }

    private int getIntStepNumber(Step step) {
	return this.getStepNumber(step).intValue();
    }

    private Step getStep(int number) {
	return (Step)(this.sfc.steps).get(number);
    }

    private Step getStep(Integer number) {
	return this.getStep(number.intValue());
    }
        
    private boolean transitionExists(Step source, Step target) {
	LinkedList transitionList = this.sfc.transs;
	for (int t=0; t<transitionList.size(); t++) {
	    Transition transition = (Transition)transitionList.get(t);
	    if (transition.source.indexOf(source) != -1)
		if (transition.target.indexOf(target) != -1)
		    return true;
	}
	return false;
    }

    /*
     * First place all steps in level order and after that place all
     * parallel steps on the lowest common level.
     */
    private SFC placeInLevelOrder() {

	int[] orderOfSteps = 
	    this.getOrderOfSteps();

	LinkedList parallelOrderOfSteps =
	    this.getParallelOrderOfSteps(TOP);
       	parallelOrderOfSteps.addAll(this.getParallelOrderOfSteps(BOTTOM));
	StepList.removeDoublyEntries(parallelOrderOfSteps);
	this.debug.writeln("Parallel Top & Bottom "+parallelOrderOfSteps);

	for (int i=0; i<parallelOrderOfSteps.size(); i++) {
	    int max = -1;
	    StepList listOfParallelSteps =
		(StepList)parallelOrderOfSteps.get(i);
	    for (int j=0; j<listOfParallelSteps.size(); j++) {
		Step step = listOfParallelSteps.get(j);
		int k = this.getIntStepNumber(step);
		if (orderOfSteps[k] > max)
		    max = orderOfSteps[k];
	    }
	    for (int j=0; j<listOfParallelSteps.size(); j++) {
		Step step = listOfParallelSteps.get(j);
		int k = this.getIntStepNumber(step);
		orderOfSteps[k] = max;
	    }
	}
	this.debug.write("Max-Depth-Order: ");
	for (int i=0; i<orderOfSteps.length; i++)
	    this.debug.write(orderOfSteps[i]+" ");
	this.debug.writeln();
	
	// FIX-ME: maxDepth nicht amountOfSteps
	StepList[] bucketSortList = new StepList[this.sfc.steps.size()];
	for (int i=0; i<this.sfc.steps.size(); i++)
	    bucketSortList[i] = new StepList();
	for (int i=0; i<this.sfc.steps.size(); i++)
	    bucketSortList[orderOfSteps[i]].add(this.getStep(i));
	//for (int i=0; i<this.sfc.steps.size(); i++)
	//    System.out.println(bucketSortList[i]);

	this.graph = bucketSortList;
	this.debug.repaint();
	DebugGraphics g = new DebugGraphics();
	this.paint(g);
	return this.sfc;
    }

    private int[] getOrderOfSteps() {

	int       size   = this.sfc.steps.size();
       	int[]     level  = new int[size];
       	boolean[] marked = new boolean[size];
	StepList  queue  = new StepList();
	int       iStepNumber = this.getIntStepNumber(this.sfc.istep);
	queue.add(this.sfc.istep);
	marked[iStepNumber] = true;
	this.debug.write("Level-Order: ");
	while (queue.size() > 0) {
	    //System.out.println(queue);
	    Step sourceStep = queue.remove();
	    int source = this.getIntStepNumber(sourceStep);
	    this.debug.write(source+" ");
	    int depth = level[source];
	    //marked[source] = true;
	    for (int target=0; target<size; target++) 
		if (target!=iStepNumber) {
		    Step targetStep = (Step)this.sfc.steps.get(target);
		    if (this.transitionExists(sourceStep, targetStep))
			if (level[target]<=depth) {
			    level[target] = (depth+1) % size;
			    if (!marked[target]) {
				queue.add(this.getStep(target));
				marked[target] = true;
			    }
			}
		}
	}
	this.debug.writeln();
	this.debug.write("Depth-Order: ");
	for (int step=0; step<marked.length; step++)
	    this.debug.write(level[step]+" ");
	this.debug.writeln();
	return level;
    }	

    private LinkedList getParallelOrderOfSteps(boolean type) {
	LinkedList parallelOrderOfSteps = new LinkedList();
	LinkedList transitionList       = this.sfc.transs; 
	for (int ts=0; ts<transitionList.size(); ts++) {
	    Transition transition = (Transition)transitionList.get(ts);
	    LinkedList steps;
	    if (type == BOTTOM)
		steps = transition.source;
	    else
		steps = transition.target;
	    if (steps.size() > 1) {
		StepList parallelSteps = new StepList();
		for (int i=0; i<steps.size(); i++)
		    parallelSteps.add((Step)steps.get(i));
		parallelOrderOfSteps.add(parallelSteps);
	    }
	}
	return parallelOrderOfSteps;
    }

    /**
     * For testing only! Will be removed in the final version of this package.
     */
    protected void paint(Graphics g) {
	if (this.graph == null)
	    return;

	

	/*
	 * Initialize this way:
	 *
	 * ##
	 * #
	 * ###
	 * ##
	 */
	for (int i=0; i<this.graph.length; i++) {
	    StepList stepList = this.graph[i];
	    for (int s=0; s<stepList.size(); s++) {
		Step target = stepList.get(s);
		Position pos = new Position((float)(s*(this.STEP_WIDTH)),
					    (float)(i*(this.STEP_HEIGHT+40)));
		//if (i == 2)
		//    pos = new Position((float)((s+1)*(this.STEP_WIDTH+10)),
		//		       (float)(20+i*(this.STEP_HEIGHT+10)));
		target.pos = pos;
	    }
	}
	
	for (int i=0; i<this.graph.length; i++) {
//	for (int i=0; i<5; i++) {
	    StepList stepList = this.graph[i];
	    for (int s=0; s<stepList.size(); s++) {
		int x = 0;
		int n = 0;
		Step sourceStep = stepList.get(s);
		int source = this.getIntStepNumber(sourceStep);
		for (int target=0; target<this.sfc.steps.size(); target++) {
		    Step targetStep = (Step)this.sfc.steps.get(target);
		    if (this.transitionExists(sourceStep, targetStep)) {
			x += (int)(targetStep.pos.x);
			n += 1;
		    }
		}
		this.debug.writeln(i+" "+s+" "+n+" "+x+" "+source);
		if (n == 0)
		    x = 0;
		else
		    x = (x)/ n;
		if (s > 0) {
		    int preSource = this.getIntStepNumber(stepList.get(s-1));
		    Step target = (Step)this.sfc.steps.get(preSource);
		    int preX = (int)(target.pos.x);
		    int stepWidth = target.name.length()*8+8;
		    if (stepWidth < 30)
			stepWidth = 30;
		    LinkedList actions = target.actions;
		    if (actions.size() != 0) {
			stepWidth += 10;
			int max = 0;
			for (int o=0; o<actions.size(); o++) {
			    StepAction stepAction = (StepAction)actions.get(o);
			    int length = stepAction.a_name.length();
			    if (length > max)
				max = length;
			}
			stepWidth += max*7 + 8;
		    }
		    if ((preX+stepWidth) > x)
			x = preX+stepWidth+40;
		} 
		Step sourceStep2 = (Step)this.sfc.steps.get(source);
		this.debug.writeln(x);
		sourceStep2.pos.x = x;
	    }
	}
	return;
	//this.paintSteps(g, this.graph);
    }

    private void paintSteps(Graphics g, StepList[] graph) {
	for (int i=0; i<graph.length; i++) {
	    for (int s=0; s<graph[i].size(); s++) {
		this.paintStep(g, graph[i].get(s));
	    }
	}
    }

    private void paintStep(Graphics g, Step step) {
	String text = step.name;
	int    x0   = (int)(step.pos.x);
	int    y0   = (int)(step.pos.y);
	g.drawString(text, x0+4, y0+5+STEP_HEIGHT/2);
	g.drawRect(x0, y0, STEP_WIDTH, STEP_HEIGHT);
	if (step == this.sfc.istep)
	    g.drawRect(x0+2, y0+2, STEP_WIDTH-4, STEP_HEIGHT-4);
    }
}    












