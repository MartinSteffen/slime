package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc output actions. An output action consists of a qualifier
 * and a program to be executed.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: OutputAction.java,v 1.1 2002-06-25 05:23:33 swprakt Exp $
 */


public class OutputAction extends Absynt implements Serializable { 
    public String   a_name;
    public Variable a_var;
    public Expr     a_expr;

  public OutputAction (String _a_name, Variable _a_var, Expr _a_expr) {
    a_name = _a_name;
    a_var  = _a_var;
    a_expr = _a_expr;
  }
  
}




