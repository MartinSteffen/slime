
package slime.checks;

import java.lang.*;
import slime.absynt.*;


/** Generic exception for the check class.
 * More specific exceptions are subclasses from this one.
 * Since the checks will be implemented as visitor to the abstract
 * syntax, the most general exception must be a subclass of
 * the general visitor exception offered by the abstract syntax.
 *
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version  $Id: CheckException.java,v 1.9 2002-07-11 08:39:53 swprakt Exp $
 */
public class CheckException extends Exception {
  /** contains exception message
   */
  public  String message = "General exception of the checks-package";
  /** access the message of the exception
   * @return exception message
   */
  public String getMessage(){return message;  }
}
    

//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.8  2002/07/07 14:57:45  swprakt
//    ok for today [Steffen]
//
//    Revision 1.7  2002/07/05 16:50:54  swprakt
//    ok
//
//    Revision 1.6  2002/06/19 20:20:25  swprakt
//    ?
//
//    Revision 1.5  2002/06/19 14:13:01  swprakt
//    added 2 constructors [Thomas Richter]
//
//    Revision 1.4  2002/06/13 12:34:28  swprakt
//    Started to add vistors + typechecks [M. Steffen]
//
//    Revision 1.3  2002/06/13 09:28:51  swprakt
//    OK
//
//    Revision 1.2  2002/06/12 18:51:59  swprakt
//    reorganization of the package-structure
//
//        src/<package>  => src/slime/<package>
//
//
//    as decided in the group meeting. consequently, some adaption had to be done
//    (wrt. import, package name etc). It compiles again.
//
//    [M. Steffen]
//
//    Revision 1.1  2002/06/12 07:04:47  swprakt
//    *** empty log message ***
//
//
//---------------------------------------------------------------------


