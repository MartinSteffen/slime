package slime.editor;

import slime.absynt.*;
import java.util.LinkedList;

/**
 * An object of this class is used for storing different ESFC's in a list.
 * <br><br>
 * Status: complete <br>
 * Known bugs: - <br>
 * @author Andreas Niemann
 * @version $Id: ESFCList.java,v 1.3 2002-06-14 11:21:03 swprakt Exp $
 */

public final class ESFCList {

    private LinkedList eSFCList;

    /**
     * Constructs an empty list of ESFC's.
     */
    protected ESFCList() {
	this.eSFCList     = new LinkedList();
    }

    /**
     * Adds the given ESFC iff it is not contained in this eSFCList.
     * @return True iff the ESFC was added.
     */ 
    protected boolean add(ESFC eSFC) {
	if (!this.contains(eSFC)) {
	    this.eSFCList.add(eSFC);
	    return true;
	}
	return false;
    }

    /**
     * Gets the ESFC from the specified position of this eSFCList. 
     * or null if the position is out of range.
     * @param pos Position of the ESFC in this list.
     * @return The selected ESFC or null if the position is out of range.
     */
    protected ESFC get(int pos) {
	if ((pos >= 0) && (pos < this.eSFCList.size()))
	    return (ESFC)this.eSFCList.get(pos);
	else
	    return null;
    }

    /**
     * Checks if this list contains the given ESFC. 
     */
    protected boolean contains(ESFC eSFC) {
	return (this.eSFCList.indexOf(eSFC) != -1);
    }
}
    








