package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * SFC.java
 * Class for sfc-programs, the top level syntactic construct,
 * i.e., the entry point.
 * @author Initially provided by Martin Steffen.
 * @version $Id: SFC.java,v 1.1 2002-04-16 13:53:52 swprakt Exp $
 */


public class SFC extends Absynt implements Serializable{ 
  public Step istep;
  public LinkedList steps;
  public LinkedList transs;
  public LinkedList actions;
  public LinkedList declist;
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
}




//----------------------------------------------------------------------
//	Abstract Syntax for Snot Programs
//	------------------------------------
//
//	$Id: SFC.java,v 1.1 2002-04-16 13:53:52 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

