package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc output actions. An output action consists of a qualifier
 * and a program to be executed.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: OutputAction.java,v 1.5 2002-07-10 14:03:22 swprakt Exp $
 */


public class OutputAction extends Stmt implements Serializable { 
    public String   a_name;
    public Variable a_var;
    public Expr     a_expr;

  public OutputAction (String _a_name, Variable _a_var, Expr _a_expr) {
    a_name = _a_name;
    a_var  = _a_var;
    a_expr = _a_expr;
  }

  /** visitor acceptor - just for compiling, this IS NOT the real thing
*/
    public Object accept (Visitors.IStmt ask) throws Exception {
	return null; // ask.forOutput(a_var,a_expr);
    }
	     
  
}




