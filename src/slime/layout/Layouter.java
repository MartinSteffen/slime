package layout;

import absynt.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.awt.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: Layouter.java,v 1.1 2002-04-26 12:09:13 swprakt Exp $
 */

public final class Layouter {

    private static final Integer NO_STEP     = new Integer(-1);
    private static final int     STEP_HEIGHT = 30;
    private static final int     STEP_WIDTH  = 30;

    private LinkedList[] graph;
    private SFC          sfc;
    private Step         initialStep;
    private int          amountOfSteps;
    private Hashtable    hashtable;
    private boolean[][]  adjazensMatrix;

    private DebugWindow  debug;

    private Layouter(SFC sfc) {

	this.debug = new DebugWindow(this);

	this.sfc = sfc;
	this.setSFCAttributes();
	this.createHashtable();
	this.createAdjazensMatrix();

	this.debug.writeln("Hashtable: "+this.hashtable);
	this.debug.writeln("iStep: "+this.hashtable.get(sfc.istep));
    }

    /**
     * This method applies a graphical layout algorithm on the given sfc.
     * For testing the result is shown in an own window.
     * (Do not expect to much, it is still VERY experimental)
     * @param sfc a checked SFC.
     * @return the sfc positioned.
     */
    public static SFC position_sfc(SFC sfc) {
        return (new Layouter(sfc)).placeInLevelOrder();
    }

    private void setSFCAttributes() {
	this.debug.writeln("Setting SFC attributes ...");
	this.initialStep   = this.sfc.istep;
	this.amountOfSteps = this.sfc.steps.size();
    }

    /**
     * Returns a hashtable with the Steps as keys and the ...
     */
    private void createHashtable() {
	this.debug.writeln("Creating hashtable with steps as keys ...");
	this.hashtable = new Hashtable();
	for (int nr=0; nr<this.sfc.steps.size(); nr++) { 
	    Step step = (Step)(this.sfc.steps).get(nr);
	    hashtable.put(step, new Integer(nr));
	}
    }

    private void createAdjazensMatrix() {
	this.debug.writeln("Creating adjazens matrix  ...");
	this.adjazensMatrix = 
	    new boolean[this.amountOfSteps][this.amountOfSteps];
	LinkedList  transitionList = this.sfc.transs; 
	for (int ts=0; ts<transitionList.size(); ts++) {
	    Transition transition = (Transition)transitionList.get(ts);
	    LinkedList source = transition.source;
	    LinkedList target = transition.target;
	    for (int s=0; s<source.size(); s++) {
		Integer sNr = this.getStepNumber((Step)source.get(s));
		for (int t=0; t<target.size(); t++) {
		    Integer tNr = this.getStepNumber((Step)target.get(t));
		    adjazensMatrix[sNr.intValue()][tNr.intValue()] = true;
		}
	    }
	}
    }

    private Integer getStepNumber(Step step) {
	return (Integer)this.hashtable.get(step);
    }

    private SFC placeInLevelOrder() {

	int[] orderOfSteps = 
	    this.getOrderOfSteps();

	LinkedList levelOrderOfSteps = 
	    this.getLevelOrderOfSteps();
	this.debug.writeln("Level "+levelOrderOfSteps);

	LinkedList parallelOrderOfTopSteps = 
	    this.getParallelOrderOfTopSteps();
	this.debug.writeln("Parallel Top "+parallelOrderOfTopSteps);

	LinkedList parallelOrderOfBottomSteps = 
	    this.getParallelOrderOfBottomSteps();
        this.debug.writeln("Parallel Bottom "+parallelOrderOfBottomSteps);

	LinkedList parallelOrderOfSteps =
	    parallelOrderOfTopSteps;
	parallelOrderOfSteps.addAll(parallelOrderOfBottomSteps);
	Layouter.removeEntriesWithSizeOne(parallelOrderOfSteps);
	Layouter.removeDoublyEntries(parallelOrderOfSteps);
	this.debug.writeln("Parallel Top & Bottom "+parallelOrderOfSteps);

	for (int i=0; i<parallelOrderOfSteps.size(); i++) {
	    int max = -1;
	    LinkedList listOfParallelSteps =
		(LinkedList)parallelOrderOfSteps.get(i);
	    for (int j=0; j<listOfParallelSteps.size(); j++) {
		int k = ((Integer)listOfParallelSteps.get(j)).intValue();
		if (orderOfSteps[k] > max)
		    max = orderOfSteps[k];
	    }
	    for (int j=0; j<listOfParallelSteps.size(); j++) {
		int k = ((Integer)listOfParallelSteps.get(j)).intValue();
		orderOfSteps[k] = max;
	    }
	}
	for (int i=0; i<orderOfSteps.length; i++)
	    this.debug.write(orderOfSteps[i]+" ");
	this.debug.writeln("*");
	
	// FIX-ME: maxDepth nicht amountOfSteps
	LinkedList[] bucketSortList = new LinkedList[this.amountOfSteps];
	for (int i=0; i<this.amountOfSteps; i++)
	    bucketSortList[i] = new LinkedList();
	for (int i=0; i<this.amountOfSteps; i++)
	    bucketSortList[orderOfSteps[i]].add(new Integer(i));
	for (int i=0; i<this.amountOfSteps; i++)
	    this.debug.writeln(bucketSortList[i].toString());

	this.graph = bucketSortList;
	this.debug.repaint();
	return this.sfc;
    }

    private static LinkedList getMaxListWithInteger(
	LinkedList l1, 
	LinkedList l2, 
	Integer i) {
	
	LinkedList maxL1 = Layouter.getMaxListWithInteger(l1, i);
	LinkedList maxL2 = Layouter.getMaxListWithInteger(l2, i);
	if (maxL1.size() >= maxL2.size())
	    return maxL1;
	else
	    return maxL2;
    }

    private static LinkedList getMaxListWithInteger(LinkedList l, 
						    Integer i) {

	LinkedList maxList = new LinkedList();
	for (int j=0; j<l.size(); j++) {
	    LinkedList list = (LinkedList)l.get(j);
	    if (list.size() >= maxList.size())
		if (integerIsInList(list, i))
		    maxList = list;
	}
	return maxList;
    }

    private static boolean integerIsInList(LinkedList l, 
					   Integer i) {
	for (int k=0; k<l.size(); k++) {
	    Integer j = (Integer)l.get(k);
	    if (i.intValue() == j.intValue())
		return true;
	}
	return false;
    }

    private int[] getOrderOfSteps() {

	LinkedList levelOrderOfSteps = new LinkedList();
	LinkedList statesOnSameLevel = new LinkedList();
       	int[]      level  = new int[this.sfc.steps.size()];
       	boolean[]  marked = new boolean[this.sfc.steps.size()];
	LinkedList queue  = new LinkedList();
	Integer    iStep  = this.getStepNumber(this.initialStep);
	queue.add(iStep);
	while (queue.size() > 0) {
	    Integer c = (Integer)queue.removeFirst();
	    int i = c.intValue();
	    int depth = level[i];
	    marked[i] = true;
	    for (int j=0; j<this.sfc.steps.size(); j++) 
		if (j!=iStep.intValue())
		    if (adjazensMatrix[i][j])
			if (level[j]<=depth) {
			    level[j] = depth+1;
			    if (!marked[j])
				queue.addLast(new Integer(j));
			}
	}
	for (int i=0; i<marked.length; i++)
	    this.debug.write(level[i]+" ");
	this.debug.writeln("*");
	return level;
    }	

    private LinkedList getLevelOrderOfSteps() {
	this.debug.writeln("Calculating level order os steps ...");

	LinkedList levelOrderOfSteps = new LinkedList();
	LinkedList statesOnSameLevel  = new LinkedList();
       	boolean[]  marked = new boolean[this.sfc.steps.size()+1];
	LinkedList queue  = new LinkedList();
	queue.add(this.getStepNumber(this.initialStep));
	queue.add(NO_STEP);
	marked[this.getStepNumber(this.initialStep).intValue()] = true;

	while (queue.size() > 1) {
	    Integer c = (Integer)queue.removeFirst();
	    if (c == NO_STEP) {
		levelOrderOfSteps.add(statesOnSameLevel);
		//statesOnSameLevel = new LinkedList();
		queue.addLast(NO_STEP);
	    } else {
		statesOnSameLevel.add(c);
		int i = c.intValue();
		for (int j=0; j<this.sfc.steps.size(); j++) 
		    if (adjazensMatrix[i][j])
			if (!marked[j]) {
			    marked[j] = true;
			    queue.addLast(new Integer(j));
			}
	    }
	}
	levelOrderOfSteps.add(statesOnSameLevel);
	//return levelOrderOfSteps;
	return statesOnSameLevel;
    }	

    private LinkedList getParallelOrderOfTopSteps() {
	this.debug.writeln("Calculating parallel order of top steps ...");
	LinkedList parallelOrderOfSteps = new LinkedList();
	for (int i=0; i<this.amountOfSteps; i++) { 
	    LinkedList parallelSteps = new LinkedList();
	    for (int j=0; j<this.amountOfSteps; j++) {
		if (this.adjazensMatrix[i][j])
		    parallelSteps.add(new Integer(j));
	    }
	    parallelOrderOfSteps.add(parallelSteps);
	}
	Layouter.removeEntriesWithSizeOne(parallelOrderOfSteps);
	Layouter.removeDoublyEntries(parallelOrderOfSteps);
	return parallelOrderOfSteps;
    }

    private LinkedList getParallelOrderOfBottomSteps() {
	this.debug.writeln("Calculating parallel order of bottom steps ...");
	LinkedList parallelOrderOfSteps = new LinkedList();
	for (int j=0; j<this.amountOfSteps; j++) { 
	    LinkedList parallelSteps = new LinkedList();
	    for (int i=0; i<this.amountOfSteps; i++) {
		if (this.adjazensMatrix[i][j])
		    parallelSteps.add(new Integer(i));
	    }
	    parallelOrderOfSteps.add(parallelSteps);
	}
	Layouter.removeEntriesWithSizeOne(parallelOrderOfSteps);
	Layouter.removeDoublyEntries(parallelOrderOfSteps);
	return parallelOrderOfSteps;
    }
	
    private static int getMaxSizeOfLists(LinkedList listOfLists) {
	int max = -1;

	for (int i=0; i<listOfLists.size(); i++) {
	    int size = ((LinkedList)listOfLists.get(i)).size();
	    if (size > max)
		max = size;
	}
	return max;
    }

    private static boolean integerListsAreEqual(LinkedList a, 
						LinkedList b) {
	if (a.size() != b.size())
	    return false;
	for (int i=0; i<a.size(); i++) {
	    int valueOfAI =  ((Integer)a.get(i)).intValue();
	    int valueOfBI =  ((Integer)b.get(i)).intValue();
	    if (valueOfAI != valueOfBI)
		return false;
	}
	return true;
    }

    private static LinkedList removeDoublyEntries(LinkedList listOfIntegerLists) {
	for (int i=0; i<listOfIntegerLists.size(); i++) {
	    LinkedList listOfIntegersA = 
		(LinkedList)listOfIntegerLists.get(i);
	    for (int j=i+1; j<listOfIntegerLists.size(); j++) {
		LinkedList listOfIntegersB = 
		    (LinkedList)listOfIntegerLists.get(j);
		if (integerListsAreEqual(listOfIntegersA, 
					 listOfIntegersB)) 
		    listOfIntegerLists.remove(j--);
	    }
	}
	return listOfIntegerLists;
    }

    private static LinkedList removeEntriesWithSizeOne(LinkedList listOfIntegerLists) {
	for (int i=0; i<listOfIntegerLists.size(); i++) {
	    LinkedList listOfIntegers = (LinkedList)listOfIntegerLists.get(i);
	    if (listOfIntegers.size() == 1)
		listOfIntegerLists.remove(i--);
	}
	return listOfIntegerLists;
    }

    /**
     * For testing only! Will be removed in the final version of this package.
     */
    protected void paint(Graphics g) {
	if (this.graph == null)
	    return;

	for (int i=0; i<this.graph.length; i++) {
	    LinkedList steps = this.graph[i];
	    for (int s=0; s<steps.size(); s++) {
		int source = ((Integer)steps.get(s)).intValue();
		Step target = (Step)this.sfc.steps.get(source);
		Position pos = new Position((float)(40+s*(this.STEP_WIDTH+10)),
					    (float)(40+i*(this.STEP_HEIGHT+10)));
		if (i == 2)
		    pos = new Position((float)(40+(s+1)*(this.STEP_WIDTH+10)),
				       (float)(40+i*(this.STEP_HEIGHT+10)));
		target.pos = pos;
	    }
	}
	
	for (int i=0; i<this.graph.length; i++) {
	    LinkedList steps = this.graph[i];
	    for (int s=0; s<steps.size(); s++) {
		int x = 0;
		int n = 0;
		int source = ((Integer)steps.get(s)).intValue();
		for (int j=0; j<this.amountOfSteps; j++)
		    if (this.adjazensMatrix[source][j]) {
			Step target = (Step)this.sfc.steps.get(j);
			x += (int)(target.pos.x);
			n += 1;
		    }
		this.debug.writeln(i+" "+s+" "+n+" "+x+" "+source);
		if (n == 0)
		    x = 0;
		else
		    x = (x)/ n;
		if (s > 0) {
		    int preSource = ((Integer)steps.get(s-1)).intValue();
		    Step target = (Step)this.sfc.steps.get(preSource);
		    int preX = (int)(target.pos.x);
		    if ((preX+40) > x)
			x = preX+40;
		} 
		Step sourceStep = (Step)this.sfc.steps.get(source);
		this.debug.writeln(x);
		sourceStep.pos.x = x;
	    }
	}

	Integer iStep = this.getStepNumber(this.initialStep);
	for (int i=0; i<this.graph.length; i++) {
	    LinkedList steps = this.graph[i];
	    for (int s=0; s<steps.size(); s++) {
		Integer step = (Integer)steps.get(s);
	        if (step.intValue() != -1) {
		    Step st = (Step)this.sfc.steps.get(step.intValue());
		    String text = st.name;
		    int x0 = (int)(st.pos.x);
		    int y0 = (int)(st.pos.y);
		    g.drawString(text, x0+4, y0+5+STEP_HEIGHT/2);
		    g.drawRect(x0,y0,STEP_WIDTH,STEP_HEIGHT);
		    if (step.intValue() == iStep.intValue())
			g.drawRect(x0+2,y0+2,STEP_WIDTH-4,STEP_HEIGHT-4);
		    //g.drawLine(x0, y0, x0, y0+STEP_HEIGHT/2);
		    //g.drawLine(x0, y0-STEP_HEIGHT, x0, y0-STEP_HEIGHT-STEP_HEIGHT/2);
		}
	    }
	}
    }
}    












