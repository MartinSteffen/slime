package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * A transition of an SFC connects source and target step
 * and is labelled with a guard.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Transition.java,v 1.4 2002-06-14 16:50:32 swprakt Exp $	
 */


public class Transition extends Absynt implements Serializable { 
  public LinkedList source;
  public Expr       guard;
  public LinkedList target;
  //------------------
  public Transition (LinkedList _s, Expr _g, LinkedList _t) {
    source = _s;
    guard  = _g;
    target = _t;
  };

  //overloading: empty guard = true
  public Transition (LinkedList _s, LinkedList _t) {
    source = _s;
    guard  = new Constval (true);
    target = _t;
};

  // ----------------------------------------------
  public Object accept(Visitors.ITransition ask) throws Exception {
    return ask.forTransition(source, guard, target);
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Transition.java,v 1.4 2002-06-14 16:50:32 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:57  swprakt
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
//	Revision 1.2  2002/04/16 19:02:56  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:57  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

