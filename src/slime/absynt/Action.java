package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc actions. An action consists of a qualifier
 * and a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: Action.java,v 1.3 2002-06-12 18:51:52 swprakt Exp $
 */


public class Action  extends Absynt implements Serializable { 
  public String   a_name;
  public LinkedList sap;    // simple assignement program 

  public Action (String _a_name, LinkedList _sap) {
    a_name = _a_name;
    sap    = _sap;
  }
  
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Action.java,v 1.3 2002-06-12 18:51:52 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:52  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:43  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

