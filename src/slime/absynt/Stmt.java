package slime.absynt;
import java.io.Serializable;


/**
 * Statements for SFC's as part of (very) simple assignement language.
 * The statements are implemented as abstract class, with
 * the concrete classes as subclasses.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Stmt.java,v 1.4 2002-06-25 08:01:17 swprakt Exp $
 */


/** Visitor acceptor
 */
public abstract class Stmt extends Absynt implements Serializable { 
  public abstract Object accept(Visitors.IStmt ask) throws Exception;
}


//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Stmt.java,v 1.4 2002-06-25 08:01:17 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:57  swprakt
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
//	Revision 1.2  2002/04/16 19:02:56  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:56  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

