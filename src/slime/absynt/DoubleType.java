package slime.absynt;
import java.io.Serializable;


/**
 * Type for doubles
 * 
 * @author initially provided by Marco Wendel
 * @version $Id: DoubleType.java,v 1.5 2002-07-10 14:03:22 swprakt Exp $
 */
 /*
 * $Log: not supported by cvs2svn $
 * Revision 1.4  2002/07/10 13:56:14  swprakt
 * Removed accept. (mwe)
 *
 * Revision 1.3  2002/07/04 16:30:42  swprakt
 * I hope that Martin was not working on them the same time:)
 * Updated Visitors Interfaces for Statements (forInput & forOutput,
 * forDouble), extended Expr. now we have support for power and
 * modulo. (mwe)
 *
 * Revision 1.2  2002/06/28 08:03:08  swprakt
 * old versions did conflict with "global" Makefile
 * in src/slime, albeit the GLOBAL Makefile should
 * exist in src. (mwe)
 *
 * Revision 1.1  2002/06/27 20:29:01  swprakt
 * Proposed DoubleType.
 *
 */


public class DoubleType  extends Type implements Serializable { 
  /** 
    *  visitor acceptor
 */
    public Object accept (Visitors.IType ask) throws Exception {
	// I decided to return null, because we discussed
	// the thing about this visitor-stuff. Our decission
	// was to add DoubleType. Try to compile the older version:)
	return null; // ask.forDoubleType();
    }
  
}

