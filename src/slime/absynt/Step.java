package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * A step of a Snot-program.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Step.java,v 1.1 2002-04-16 13:53:54 swprakt Exp $
 */


public class Step extends Absynt implements Serializable { 
  public String name;
  public LinkedList actions;
  public Position pos;
  //-------------------------------
  public Step (String _name) {
    name = _name;
    actions = new LinkedList();
  }
  public Step (String _name, LinkedList _actions) {
    name = _name;
    actions = _actions;
  }
  public Step (String _name, LinkedList _actions, Position _pos) {
    name = _name;
    actions = _actions;
    pos     = _pos;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Step.java,v 1.1 2002-04-16 13:53:54 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

