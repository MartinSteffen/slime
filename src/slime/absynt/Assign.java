package slime.absynt;
import java.io.Serializable;


/**
 * An assignment-statement as part of the simple assignment language
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Assign.java,v 1.3 2002-06-12 18:51:53 swprakt Exp $
 */


public class Assign extends Stmt implements Serializable { 
  public Variable var;
  public Expr     val;


  public Assign (Variable x, Expr e) {
    var = x;
    val = e;
  };
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Assign.java,v 1.3 2002-06-12 18:51:53 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:53  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:44  swprakt
//	Slime initial version
//	
//	
//---------------------------------------------------------------------

