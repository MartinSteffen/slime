
package slime.checks;

import java.lang.*;
import slime.absynt.*;


/** Exceptions to be thrown by the type checker.
 * The type checker's exceptions are inner classes of
 * this class.
 *
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version $Id: Typeerrors.java,v 1.3 2002-06-19 23:00:10 swprakt Exp $
 */
public class Typeerrors {
    public class TypecheckException extends CheckException {
    }
    
    public class UnbooleanGuard extends TypecheckException {
    }
    
    public class UndeclaredVarible extends TypecheckException{
    }
    
}


//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.2  2002/06/14 11:59:10  swprakt
//    *** empty log message ***
//
//    Revision 1.1  2002/06/14 09:22:52  swprakt
//    *** empty log message ***
//
//
//---------------------------------------------------------------------