package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Visitors for Slime abstract syntax (just for the fun of it)
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version  $Id: Visitors.java,v 1.3 2002-06-14 16:50:32 swprakt Exp $
 */


public class Visitors {
  
  public interface ISFC{
    public Object forSFC(Step istep, 
			 LinkedList steps,
			 LinkedList transs,
			 LinkedList actions, 
			 LinkedList declist) throws Exception;
  }

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
//	$Id: Visitors.java,v 1.3 2002-06-14 16:50:32 swprakt Exp $
// //	$Log: not supported by cvs2svn $
//---------------------------------------------------------------------





