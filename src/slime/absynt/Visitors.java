package slime.absynt;
import java.io.Serializable;

/**
 * Visitors for Slime abstract syntax (just for the fun of it)
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version  $Id: Visitors.java,v 1.1 2002-06-13 12:34:27 swprakt Exp $
 */


public class Visitors {
  public interface IExpr{
    public Object forB_Expr(Expr l, int o, Expr r) throws Exception;
    public Object forU_Expr(Expr s, int o)         throws Exception;
    public Object forVariable(String s, Type t)    throws Exception;
    public Object forConstval(Object v)            throws Exception;
  }

}



//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Visitors.java,v 1.1 2002-06-13 12:34:27 swprakt Exp $
//
//---------------------------------------------------------------------





