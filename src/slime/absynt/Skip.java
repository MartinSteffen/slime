package slime.absynt;
import java.io.Serializable;  // for instance 


/**
 * A skip statement as part of the simple assignement language
 * @author Initially provided by Martin Steffen
 * @version $Id: Skip.java,v 1.4 2002-06-25 08:01:17 swprakt Exp $
 */


public class Skip extends Stmt implements Serializable { 
  // -------------
  public Skip () {};    // in principle, covered by the default constructor.

  /** visitor acceptor
   */
  public Object accept (Visitors.IStmt ask) throws Exception {
    return ask.forSkip();
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	-----------------------------------
//
//	$Id: Skip.java,v 1.4 2002-06-25 08:01:17 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:56  swprakt
//	reorganization of the package-structure
//	
//		src/<package>  => src/slime/<package>
//	
//	
//	as decided in the group meeting. consequently, some adaption had to be done
//	(wrt. import, package name etc). It compiles again.
//	
//	[M. Steffen]
//	
//	Revision 1.2  2002/04/16 19:02:55  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:54  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

