package slime.absynt;
import java.io.Serializable;


/**
 * @author Initially provided by Martin Steffen.
 * @version $Id: Nqual.java,v 1.4 2002-06-25 06:09:04 swprakt Exp $
 */


public class Nqual extends ActionQualifier implements Serializable { 
  /** 
      visitor acceptor
  */
  public Object accept (Visitors.IActionQualifier ask) throws Exception {
    return ask.forNqual();
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Nqual.java,v 1.4 2002-06-25 06:09:04 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:55  swprakt
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
//	Revision 1.1  2002/04/16 13:53:51  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

