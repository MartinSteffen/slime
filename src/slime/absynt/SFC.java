package slime.absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * SFC.java
 * Class for sfc-programs, the top level syntactic construct,
 * i.e., the entry point.
 * @author Initially provided by Martin Steffen.
 * @version $Id: SFC.java,v 1.4 2002-06-14 06:37:39 swprakt Exp $
 */


public class SFC extends Absynt implements Serializable{ 
  public Step istep;
  public LinkedList steps;
  public LinkedList transs;
  public LinkedList actions;
  public LinkedList declist;
  public String name = "unnamed";

  // -------------------

  /** 
   * Constructor just stores the arguments into the fields
   **/
  public SFC (Step _istep,
	      LinkedList _steps, 
	      LinkedList _transs,
	      LinkedList _actions,
	      LinkedList _declist) {
    istep  = _istep;
    steps  = _steps;
    transs = _transs;
    actions = _actions;
    declist = _declist;
  }

  public SFC (Step _istep,
	      LinkedList _steps, 
	      LinkedList _transs,
	      LinkedList _actions,
	      LinkedList _declist,
	      String     _name) {
    istep  = _istep;
    steps  = _steps;
    transs = _transs;
    actions = _actions;
    declist = _declist;
    name    = _name;
  }
  
  /** 
   * Constructor creates empty LinkedLists
   **/
  public SFC() {
    istep = null;
    steps  = new LinkedList();
    transs = new LinkedList();
    actions = new LinkedList();
    declist = new LinkedList();      
  }

  public SFC(String _name) {
    istep = null;
    steps  = new LinkedList();
    transs = new LinkedList();
    actions = new LinkedList();
    declist = new LinkedList();      
    name    = _name;
  }
}




//----------------------------------------------------------------------
//	Abstract Syntax for Slime Programs
//	------------------------------------
//
//	$Id: SFC.java,v 1.4 2002-06-14 06:37:39 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:56  swprakt
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
//	Revision 1.2  2002/04/16 19:02:55  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:52  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

