package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * A step of a Slime-program.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Step.java,v 1.3 2002-06-12 18:51:56 swprakt Exp $
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
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Step.java,v 1.3 2002-06-12 18:51:56 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:56  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:54  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

