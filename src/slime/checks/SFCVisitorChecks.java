
package slime.checks;

import java.lang.*;
import slime.absynt.*;


/** Top-level class providing the functionality for the static
 * analysis par for Slime.
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: SFCVisitorChecks.java,v 1.1 2002-06-19 20:14:16 swprakt Exp $
 */
public class SFCVisitorChecks implements SFCVisitorInterface {
    public boolean isWellTyped(SFC p) throws CheckException {
        return true;
    }
    
    public  boolean isWellformed(SFC p) throws CheckException {
        return true;
    }
    
    public boolean isEmpty(SFC p)throws CheckException {
        return true;
    }
}



//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.5  2002/06/19 19:40:48  swprakt
//    IChecks -> SFCVisitorInterface [Thomas Richter]
//
//    Revision 1.4  2002/06/12 18:52:00  swprakt
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
//    Revision 1.3  2002/06/12 15:06:08  swprakt
//    OK
//
//    Revision 1.2  2002/06/12 09:20:03  swprakt
//    cosmetics (M. Steffen)
//
//    Revision 1.1  2002/06/12 09:18:56  swprakt
//    First version: compilable stub code. The checks do nothing, they
//    optimistically return true, no exceptions.
//
//    M. Steffen
//
//
//---------------------------------------------------------------------
