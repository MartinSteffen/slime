package slime.absynt;
import java.io.Serializable;


/**
 * Types for the assgnement language of Slime, with the concrete
 * types as subclasses.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Type.java,v 1.5 2002-07-10 14:06:28 swprakt Exp $
 */

public abstract class Type extends Absynt implements Serializable { 
  //  public abstract Object accept (Visitors.IType ask) throws Exception;
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Type.java,v 1.5 2002-07-10 14:06:28 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2002/06/24 20:08:11  swprakt
//	Types extended with visitor methods,
//	also extended is the type ckecker, which is a visitor to the absynt.
//	
//	[Steffen]
//	
//	Revision 1.3  2002/06/12 18:51:58  swprakt
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
//	Revision 1.1  2002/04/16 13:53:57  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

