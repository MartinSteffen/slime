package slime.checks;
import java.lang.*;
import slime.absynt.*;


/** Generic exception for the check class.
 * More specific exceptions are subclasses from this one.
 * Since the checks will be implemented as visitor to the abstract
 * syntax, the most general exception must be a subclass of
 * the general visitor exception offered byte the abstract syntax.
 *
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: CheckException.java,v 1.5 2002-06-19 14:13:01 swprakt Exp $
 */
public class CheckException extends Exception {
    /** contains exception message
     */
    protected String message;
    
    /** access the message of the exception
     * @return exception message
     */
    public String getMessage(){
        return message;
    }
    
    /** set the message of the exception (internal)
     * @param msg exception message
     */
    private void setMessage(String msg) {
        message = msg;
    }
    
    /** Creates new <code>CheckException</code> without detail message.
     */
    public CheckException() {
        super("Check Exception");
    }
    
    
    /** Constructs an <code>CheckException</code> with the specified detail message.
     * @param msg the detail message
     */
    public CheckException(String msg) {
        super(msg);
        setMessage(msg);
    }
}


//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
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
