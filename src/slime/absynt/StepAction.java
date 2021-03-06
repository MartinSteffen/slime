package slime.absynt;
import java.io.Serializable;


/**
 * Class for sfc actions associated with a step. An action consists of a
 * qualifier and the name of a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepAction.java,v 1.4 2002-06-25 05:48:26 swprakt Exp $
 */


public class StepAction  extends Absynt implements Serializable { 
  public ActionQualifier qualifier;
  public String          a_name;    // name of program 

  public StepAction (ActionQualifier _qualifier, String _a_name) {
    qualifier = _qualifier;
    a_name    = _a_name;
  }

  // ----------------------------------------------
  public Object accept(Visitors.IStepAction ask) throws Exception {
    return ask.forStepAction(qualifier, a_name);
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: StepAction.java,v 1.4 2002-06-25 05:48:26 swprakt Exp $
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
//	Revision 1.1  2002/04/16 13:53:55  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

