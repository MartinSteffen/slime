package slime.layout;

import slime.absynt.*;
import java.util.LinkedList;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: StepList.java,v 1.4 2002-06-14 11:21:05 swprakt Exp $
 */

class StepList {

    private LinkedList stepList;

    protected StepList() {
	this.stepList = new LinkedList();
    }

    protected void add(Step step) {
	this.stepList.add(step);
    }

    protected Step get(int nr) {
	return (Step)this.stepList.get(nr);
    }

    /*
     * Returns true iff this steplist contains the given step object. 
     */
    protected boolean contains(Step step) {
	return (this.stepList.indexOf(step) != -1);
    }

    protected Step remove() {
	return (Step)this.stepList.removeFirst();
    }

    protected int size() {
	return this.stepList.size();
    }

    /*
     * Returns true iff this steplist contains the same step objects
     * as the given steplist.
     */
    protected boolean equal(StepList secondStepList) {
	if (this.stepList.size() == secondStepList.size())
	    for (int i=0; i<this.stepList.size(); i++)
		if (!this.stepList.contains(secondStepList.get(i)))
		    return false;
	return true;
    }

    protected static void removeDoublyEntries(LinkedList listOfStepLists) {
	for (int i=0; i<listOfStepLists.size(); i++) {
	    StepList stepList = (StepList)listOfStepLists.get(i);
	    for (int j=i+1; j<listOfStepLists.size(); j++) 
		if (stepList.equal((StepList)listOfStepLists.get(j))) 
		    listOfStepLists.remove(j--);
	}
    }
    
//      protected static void removeEntriesWithSizeOne(LinkedList listOfStepLists) {
//  	for (int i=0; i<listOfStepLists.size(); i++) {
//  	    StepList stepList = (StepList)listOfStepLists.get(i);
//  	    if (stepList.size() == 1)
//  		listOfStepLists.remove(i--);
//  	}
//      }

    public String toString() {
	return this.stepList.toString();
    }
}
    


