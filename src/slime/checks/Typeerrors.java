
package slime.checks;

import java.lang.*;
import slime.absynt.*;



/** Exceptions to be thrown by the type checker.
 * The type checker errors as all specific check exceptions and
 * thus subclasses of CheckException. They are grouped together
 * as inner classes.
 *
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version $Id: Typeerrors.java,v 1.4 2002-06-24 17:19:55 swprakt Exp $
 */
public class Typeerrors {
  public class TypecheckException extends CheckException {
  }
    
  public class UnbooleanGuard extends TypecheckException {
    String explanation = "The guard of a transition must be an expression\n of boolean type. This error indicates the occurrence of a\n well-typed guard-expression, but with a type other than a boolean." ;
  }
    
  public class UndeclaredVariable extends TypecheckException{
    String explanation = "Each variable occuring int a program must be\n covered by a type declaration for this variable where the declaration\n is unique";
    }
  
}


//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.3  2002/06/19 23:00:10  swprakt
//    no message
//
//    Revision 1.2  2002/06/14 11:59:10  swprakt
//    *** empty log message ***
//
//    Revision 1.1  2002/06/14 09:22:52  swprakt
//    *** empty log message ***
//
//
//---------------------------------------------------------------------
