package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc input actions. An input action consists of a qualifier
 * and a program to be executed.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: InputAction.java,v 1.2 2002-07-03 17:18:13 swprakt Exp $
 */


public class InputAction extends Stmt implements Serializable { 
    public String   a_name;
    public Variable a_var;
    public Expr     a_expr;

  public InputAction (String _a_name, Variable _a_var, Expr _a_expr) {
    a_name = _a_name;
    a_var  = _a_var;
    a_expr = _a_expr;
  }

  /** visitor acceptor
     */
   public Object accept (Visitors.IStmt ask) throws Exception {
       return ask.forAssign( a_var , a_expr);
  }
	     
  
}





