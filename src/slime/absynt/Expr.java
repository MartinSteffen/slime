package slime.absynt;
import java.io.Serializable;


/**
 * Abstract class for simple expressions
 * 
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: Expr.java,v 1.9 2002-07-11 07:49:50 swprakt Exp $
 */



public abstract class Expr extends Absynt implements Serializable{ 

  public Type type;;     // expressions are typed.
    /* The operators are encoded as ``constants'' in the
       class's fields. The intended meaning is implied by the name. */

  public final  static int PLUS  = 0;
  public final  static int MINUS = 1;
  public final  static int TIMES = 2;
  public final  static int DIV   = 3;
  public final  static int AND   = 4;
  public final  static int OR    = 5;
  public final  static int NEG   = 6;
  public final  static int EQ    = 7;
  public final  static int LESS  = 8;
  public final  static int GREATER = 9;
  public final  static int LEQ     = 10;
  public final  static int GEQ     = 11;
  public final  static int NEQ     = 12;
  public final  static int MOD     = 13;
  public final  static int POW     = 14;
 
  //--------------------------------------------------



   public abstract Object accept(Visitors.IExpr ask) throws Exception;
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Expr.java,v 1.9 2002-07-11 07:49:50 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.8  2002/07/04 16:30:42  swprakt
//	I hope that Martin was not working on them the same time:)
//	Updated Visitors Interfaces for Statements (forInput & forOutput,
//	forDouble), extended Expr. now we have support for power and
//	modulo. (mwe)
//	
//	Revision 1.7  2002/06/13 12:34:27  swprakt
//	Started to add vistors + typechecks [M. Steffen]
//	
//	Revision 1.6  2002/06/13 08:10:38  swprakt
//	Removed the visitor.
//	
//	Revision 1.5  2002/06/13 07:02:48  swprakt
//	accept must be public
//	
//	Revision 1.4  2002/06/12 18:51:55  swprakt
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
//	Revision 1.3  2002/06/12 15:06:06  swprakt
//	OK
//	
//	Revision 1.2  2002/04/16 19:02:54  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:49  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------



