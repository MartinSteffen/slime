package slime.absynt;
import java.io.Serializable;


/**
 * Type for doubles
 * 
 * @author initially provided by Marco Wendel
 * @version $Id: DoubleType.java,v 1.8 2002-07-10 17:04:29 swprakt Exp $
 */
 /*
 * $Log: not supported by cvs2svn $
 * Revision 1.7  2002/07/10 15:38:22  swprakt
 * The accept-method is inherited from the abstract
 * super type Type. It throws an exception, as the
 * DoubleType is currently not required in the
 * visitor-interface.
 *
 * [Steffen]
 *
 * Revision 1.6  2002/07/10 14:31:56  swprakt
 * *** empty log message ***
 *
 * Revision 1.5  2002/07/10 14:03:22  swprakt
 * Those classes MUST be declared abstract if accept(Visi...) is
 * missing, so I decided to simply return null. (Now) it is task
 * of the checker team to filter their objects correctly. (mwe)
 *
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
  // we inherit the acceptor method from the abstract class Type,
  // which throws an exception.

  public boolean equals (Type t) {
    return  (t instanceof  DoubleType);
  }


}


