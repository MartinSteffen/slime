package absynt;
import java.io.Serializable;


/**
 * binary expressions
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: B_expr.java,v 1.2 2002-04-16 19:02:53 swprakt Exp $
 */


public class B_expr   extends Expr implements Serializable{ 
  public Expr   left_expr;
  public Expr   right_expr;
  public int    op;
/**
 * The operands are ``constant'' fields of the expression superclass.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: B_expr.java,v 1.2 2002-04-16 19:02:53 swprakt Exp $
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
}




//----------------------------------------------------------------------
//	Abstract Syntax for Slime programs
//	------------------------------------
//
//	$Id: B_expr.java,v 1.2 2002-04-16 19:02:53 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2002/04/16 13:53:45  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

