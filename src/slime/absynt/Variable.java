package slime.absynt;
import java.io.Serializable;


/**
 * Variables of the simple assignement language.
 * They are very simply implemented as strings
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Variable.java,v 1.3 2002-06-12 18:51:58 swprakt Exp $	
 */


public class Variable extends Expr implements Serializable { 
  public String name;
  public Type   type;

  public Variable (String s) {
    name = s;
    type = null;
  };

  public Variable (String s, Type _t) {
    name = s;
    type  = _t;};
};




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Variable.java,v 1.3 2002-06-12 18:51:58 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:57  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:59  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

