package slime.absynt;
import java.io.Serializable;


/**
 * Type for booleans.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: BoolType.java,v 1.5 2002-07-07 08:13:30 swprakt Exp $
 */


public class BoolType  extends Type implements Serializable { 

  /** 
      visitor acceptor
  */
  public Object accept (Visitors.IType ask) throws Exception {
    return ask.forBoolType();
  }
  
  public boolean equals (Type t) {
    return  (t instanceof  BoolType);
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: BoolType.java,v 1.5 2002-07-07 08:13:30 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2002/06/24 20:08:10  swprakt
//	Types extended with visitor methods,
//	also extended is the type ckecker, which is a visitor to the absynt.
//	
//	[Steffen]
//	
//	Revision 1.3  2002/06/12 18:51:53  swprakt
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
//	Revision 1.2  2002/04/16 19:02:53  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:46  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

