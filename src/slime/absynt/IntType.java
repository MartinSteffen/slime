package slime.absynt;
import java.io.Serializable;


/**
 * Type for integers (the name ``Int'' is already used by Java,
 * of course).
 * 
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: IntType.java,v 1.5 2002-07-10 16:53:57 swprakt Exp $
 */


public class IntType  extends Type implements Serializable { 
  /** 
      visitor acceptor
  */
  public Object accept (Visitors.IType ask) throws Exception {
    return ask.forIntType();
  }

  public boolean equals (Type t) {
    return  (t instanceof  IntType);
  }
}





//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: IntType.java,v 1.5 2002-07-10 16:53:57 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2002/06/24 20:08:10  swprakt
//	Types extended with visitor methods,
//	also extended is the type ckecker, which is a visitor to the absynt.
//	
//	[Steffen]
//	
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
//	Revision 1.2  2002/04/16 19:02:54  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:50  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

