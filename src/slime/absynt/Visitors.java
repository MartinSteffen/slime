package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Visitors for Slime abstract syntax (just for the fun of it)
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version  $Id: Visitors.java,v 1.2 2002-06-14 06:37:40 swprakt Exp $
 */


public class Visitors {
  public interface IExpr{
    public Object forB_Expr(Expr l, int o, Expr r) throws Exception;
    public Object forU_Expr(Expr s, int o)         throws Exception;
    public Object forVariable(String s, Type t)    throws Exception;
    public Object forConstval(Object v)            throws Exception;
  }

  public interface ITransition{
    public Object forTransition(LinkedList source,
				Expr       guard,
				LinkedList target) throws Exception;
  }
}



//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Visitors.java,v 1.2 2002-06-14 06:37:40 swprakt Exp $
//
//---------------------------------------------------------------------





