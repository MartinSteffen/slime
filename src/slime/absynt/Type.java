package slime.absynt;
import java.io.Serializable;


/**
 * Types for the assgnement language of Slime, with the concrete
 * types as subclasses.
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.

 * @version $Id: Type.java,v 1.8 2002-07-11 05:58:34 swprakt Exp $
 */

public abstract class Type extends Absynt implements Serializable { 
   public Object accept (Visitors.IType ask) throws Exception
  { throw new  Exception("This type does not accept visititors (yet).");}

  public abstract boolean equals (Type t);
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Type.java,v 1.8 2002-07-11 05:58:34 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.7  2002/07/10 17:04:29  swprakt
//	As announced in the group meeting: I added the
//	equals method to the types. This overrides the
//	build-in equals method (from Object). Types are
//	compared to their ``class'', not according to
//	their identity as intance.
//	
//	[Steffen]
//	
//	Revision 1.6  2002/07/10 14:32:49  swprakt
//	visitor interface made smaller.
//	[Steffen/Niemann]
//	
//	Revision 1.5  2002/07/10 14:06:28  swprakt
//	We removed the abstract accept methods. [Steffen]
//	
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

