package slime.absynt;
import java.io.Serializable;


/**
 * Variables of the simple assignement language.
 * They are very simply implemented as strings
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Variable.java,v 1.5 2002-06-26 15:50:04 swprakt Exp $	
 */


public class Variable extends Expr implements Serializable { 
  public String name;
  public Type   type;
  public boolean inputvar = false;
  public boolean outputvar = false;

  public Variable (String s) {
    name = s;
    type = null;
  };

  public Variable (String s, Type _t) {
    name = s;
    type  = _t;};

  public Variable (String s, boolean _i, boolean _o) {
    name = s;
    type  = null;
    inputvar = _i;
    outputvar = _o;};


  /**
     visitor acceptor
  */
  public Object accept (Visitors.IExpr ask) throws Exception{
    return ask.forVariable(name,type);
  }
};




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Variable.java,v 1.5 2002-06-26 15:50:04 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2002/06/13 12:34:27  swprakt
//	Started to add vistors + typechecks [M. Steffen]
//	
//	Revision 1.3  2002/06/12 18:51:58  swprakt
//	reorganization of the package-structure
//	
//		src/<package>  => src/slime/<package>
//	
//	
//	as decided in the group meeting. consequently, some adaption had to be done
//	(wrt. import, package name etc). It compiles again.
//	
//	[M. Steffen]
//	
//	Revision 1.2  2002/04/16 19:02:57  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:59  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

