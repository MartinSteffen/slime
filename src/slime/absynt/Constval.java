package absynt;
import java.io.Serializable;


/**
 * Constant values as expressions.
 * Supported are integers and booleans.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Constval.java,v 1.2 2002-04-16 19:02:53 swprakt Exp $
 */


public class Constval  extends Expr implements Serializable { 
  /**
     Constant values are integers and booleans; they are directly
     implemented using the corresponding data types from Java.
   */

  public Object val;
  
  /**
   * 2 overloaded constructors. The value is stored as ``Object''
   */
  public Constval (boolean v) {
    val = new Boolean (v);}


  public Constval (int v) {
    val = new Integer (v);}
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Constval.java,v 1.2 2002-04-16 19:02:53 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2002/04/16 13:53:47  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

