package editor;

import absynt.*;
import java.util.LinkedList;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: ESFCList.java,v 1.1 2002-05-30 13:03:53 swprakt Exp $
 */

class ESFCList {

    private LinkedList eSFCList;

    protected ESFCList() {
	this.eSFCList     = new LinkedList();
    }

    /*
     * Adds the given ESFC iff it is not contains in this eSFCList.
     */ 
    protected void add(ESFC eSFC) {
	if (!this.contains(eSFC))
	    this.eSFCList.add(eSFC);
    }

    protected ESFC get(int nr) {
	return (ESFC)this.eSFCList.get(nr);
    }

    /*
     * Returns true iff this sfcList contains the given sfc object. 
     */
    protected boolean contains(ESFC eSFC) {
	return (this.eSFCList.indexOf(eSFC) != -1);
    }
}
    



