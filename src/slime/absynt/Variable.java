package absynt;
import java.io.Serializable;


/**
 * Variables of the simple assignement language.
 * They are very simply implemented as strings
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Variable.java,v 1.1 2002-04-16 13:53:59 swprakt Exp $	
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
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Variable.java,v 1.1 2002-04-16 13:53:59 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

