package slime.absynt;
import java.io.Serializable;


/**
 * Type for doubles
 * 
 * @author initially provided by Marco Wendel
 * @version $Id: DoubleType.java,v 1.3 2002-07-04 16:30:42 swprakt Exp $
 */
 /*
 * $Log: not supported by cvs2svn $
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
	return ask.forDoubleType();
    }
}

