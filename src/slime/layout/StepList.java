package layout;

import absynt.*;
import java.util.LinkedList;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: StepList.java,v 1.1 2002-05-07 12:50:15 swprakt Exp $
 */

class StepList {

    private LinkedList stepList;

    /*
     * Creates an empty steplist. 
     */
    protected StepList() {
	this.stepList = new LinkedList();
    }

    /*
     * Adds the given step to this steplist iff it is not contained. 
     */
    protected void add(Step step) {
	if (!this.contains(step))
	    this.stepList.add(step);
	//else if (DEBUG)
	    
	    
    }

    /*
     * Returns the step at the specified position in this steplist.
     */
    protected Step get(int i) {
	try {
	    return (Step)this.stepList.get(i);
	} catch (IndexOutOfBoundsException ioobe) {
	    System.out.println("Someting went wrong while getting a step "
			       +"out of a step list. The position is "+i
			       +" and the size of this steplist is "
			       +this.size()+".");
	    return null;
	}
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
    


