package absynt;
import java.io.Serializable;


/**
 * Declaration of a variables.
 * 
 * @author Initially provided by Karsten Stahl.
 * @version $Id: Declaration.java,v 1.2 2002-04-16 19:02:54 swprakt Exp $
 */


public class Declaration extends Absynt implements Serializable { 
  public Variable var;
  public Type     type;
  public Constval val;

  public Declaration (Variable _var, Type _type, Constval _val) {
    var  = _var;
    type = _type;
    val  = _val;
  }
}


//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Declaration.java,v 1.2 2002-04-16 19:02:54 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2002/04/16 13:53:47  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

