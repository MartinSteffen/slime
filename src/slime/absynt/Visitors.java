package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Visitors for Slime abstract syntax (just for the fun of it)
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version  $Id: Visitors.java,v 1.6 2002-06-24 20:08:11 swprakt Exp $
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

  public interface IDeclist{
    public Object forDeclist(LinkedList dl) throws Exception;
  }

  public interface IDeclaration{
    public Object forDeclaration(Variable var, Type type, Constval val)
      throws Exception;
  }

  public interface IType{
    public Object forIntType() throws Exception;
    public Object forBoolType() throws Exception;
    public Object forUndefType() throws Exception;
    public Object forUnitType()  throws Exception;
  }
}



//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Visitors.java,v 1.6 2002-06-24 20:08:11 swprakt Exp $
// //	$Log: not supported by cvs2svn $
// //	Revision 1.5  2002/06/24 19:30:17  swprakt
// //	added an visitor interface for types [Steffen]
// //	
// //	Revision 1.4  2002/06/24 19:14:06  swprakt
// //	I removed the class Typeerrors. It's inner classes (the
// //	exceptions for typechecking) I put into the class Typcheck.
// //	Java does not allow the previously intended setup.
// //	
// //	[Steffen]
// //	
// //	Revision 1.3  2002/06/14 16:50:32  swprakt
// //	ok, 1 week pause
// //	
//---------------------------------------------------------------------





