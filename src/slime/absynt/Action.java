package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc actions. An action consists of a qualifier
 * and a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: Action.java,v 1.1 2002-04-16 13:53:43 swprakt Exp $
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
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Action.java,v 1.1 2002-04-16 13:53:43 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

