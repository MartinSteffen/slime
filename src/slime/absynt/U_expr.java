package absynt;
import java.io.Serializable;


/**
 * Unary expressions
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: U_expr.java,v 1.1 2002-04-16 13:53:58 swprakt Exp $
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
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: U_expr.java,v 1.1 2002-04-16 13:53:58 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

