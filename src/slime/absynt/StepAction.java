package absynt;
import java.io.Serializable;


/**
 * Class for sfc actions associated with a step. An action consists of a
 * qualifier and the name of a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepAction.java,v 1.1 2002-04-16 13:53:55 swprakt Exp $
 */


public class StepAction  extends Absynt implements Serializable { 
  public ActionQualifier qualifier;
  public String          a_name;    // name of program 

  public StepAction (ActionQualifier _qualifier, String _a_name) {
    qualifier = _qualifier;
    a_name    = _a_name;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: StepAction.java,v 1.1 2002-04-16 13:53:55 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

