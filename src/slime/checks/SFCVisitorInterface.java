
package slime.checks;

import java.lang.*;
import slime.absynt.*;


/** Top-level interface for the static analysis part of the
 * Slime tool.
 * <p> Currently, only _checking_ is supported, there is no attempt
 * to derive omitted type information. Therefore, the return-type
 * of the methods is boolean. The convention is that the a return
 * value of true indicates success, a failure is indicated by
 * an exception. Int other woirds: false is never returned.</p>
 *
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: SFCVisitorInterface.java,v 1.1 2002-06-19 19:40:47 swprakt Exp $
 */
public  interface SFCVisitorInterface {
    public boolean isWellTyped(SFC p)  throws CheckException;
    public boolean isWellformed(SFC p) throws CheckException;
}



//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.7  2002/06/12 18:52:00  swprakt
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
//    Revision 1.6  2002/06/12 15:06:08  swprakt
//    OK
//
//    Revision 1.5  2002/06/12 10:07:57  swprakt
//    *** empty log message ***
//
//    Revision 1.4  2002/06/12 07:41:10  swprakt
//    Top-level/general Exceptions added to the interface.
//
//    M. Steffen
//
//    Revision 1.3  2002/06/12 07:31:02  swprakt
//    minimalistic interface (no exceptions given as yet),
//
//    compiles.
//
//    M. Steffen
//
//    Revision 1.2  2002/06/12 07:28:46  swprakt
//    *** empty log message ***
//
//    Revision 1.1  2002/06/12 07:04:47  swprakt
//    *** empty log message ***
//
//
//---------------------------------------------------------------------
