package slime.absynt;
import java.io.Serializable;


/**
 * ActionQualifier.java
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: ActionQualifier.java,v 1.4 2002-06-25 06:10:40 swprakt Exp $
 */


public abstract class ActionQualifier  extends Absynt implements Serializable {    public abstract Object accept(Visitors.IActionQualifier ask) throws Exception;
}



//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: ActionQualifier.java,v 1.4 2002-06-25 06:10:40 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:52  swprakt
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
//	Revision 1.2  2002/04/16 19:02:52  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:43  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------



