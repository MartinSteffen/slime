package slime.absynt;
import java.io.Serializable;


/**
 * Unary expressions
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: U_expr.java,v 1.3 2002-06-12 18:51:58 swprakt Exp $
 */


public class U_expr extends Expr implements Serializable{ 
  public Expr sub_expr;
  public int  op;

  public U_expr(int o, Expr s) {
    op = o;
    sub_expr = s;
  };
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: U_expr.java,v 1.3 2002-06-12 18:51:58 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:57  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:58  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

