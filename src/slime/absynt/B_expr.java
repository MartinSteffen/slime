package slime.absynt;
import java.io.Serializable;


/**
 * binary expressions
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: B_expr.java,v 1.5 2002-06-13 09:28:50 swprakt Exp $
 */


public class B_expr   extends Expr implements Serializable{ 
  public Expr   left_expr;
  public Expr   right_expr;
  public int    op;
/**
 * The operands are ``constant'' fields of the expression superclass.
 * 
 * @author Initially provided by Martin Steffen, Karsten Stahl.
 * @version $Id: B_expr.java,v 1.5 2002-06-13 09:28:50 swprakt Exp $
 */
  //---------------------------------------------------
  /**
     Constructor  in ``infix''-convention. Always call as Expr.<OP>
   */
  public B_expr (Expr l, int o,  Expr r) {
    left_expr = l;
    right_expr = r;
    op   = o;
  }

  /**
     visitor acceptor
  */
  Object accept (IExprVisitor ask) {
    return ask.forB_Expr(left_expr,op,right_expr);
  }
}




//----------------------------------------------------------------------
//	Abstract Syntax for Slime programs
//	------------------------------------
//
//	$Id: B_expr.java,v 1.5 2002-06-13 09:28:50 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2002/06/12 18:51:53  swprakt
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
//	Revision 1.2  2002/04/16 19:02:53  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:45  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

