package absynt;
import java.io.Serializable;


/**
 * Abstract class for simple expressions
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Expr.java,v 1.3 2002-06-12 15:06:06 swprakt Exp $
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


  //--------------------------------------------------

  abstract Object accept(IExprVisitor ask);
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Expr.java,v 1.3 2002-06-12 15:06:06 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:54  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:49  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------














