/*
 * CheckTestConsole.java
 *
 * Created on 19. Juni 2002, 12:53
 */

package slime.checks;

import java.lang.*;
import slime.absynt.*;
import slime.utils.*;

/** a console test tool
 * @author <a href="mailto:richter@thomas-richter.de">Thomas Richter</a>
 * @version $Id: CheckTestConsole.java,v 1.4 2002-06-19 23:00:10 swprakt Exp $
 */
public class CheckTestConsole {
    
    /** Holds value of property args. */
    private String[] args;
    
    /** Creates new CheckTestConsole */
    public CheckTestConsole() {
    }
    
    /** the main method
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        CheckTestConsole checkTestConsole = new CheckTestConsole();
        checkTestConsole.setArgs(args);
        checkTestConsole.go();
    }
    
    /** the main test method */
    public void go() {
        SFC sfc = Example.getExample1();
        if ( sfc == null ) {
            System.err.println("sfc == null");
            System.exit(1);
        }
        else
            System.err.println("scf != null");
        slime.checks.SFCVisitorChecks sFCVisitorChecks = new slime.checks.SFCVisitorChecks();
        try {
            // sFCVisitorChecks.isWellformed(sfc);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            // sFCVisitorChecks.isWellTyped(sfc);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            // sFCVisitorChecks.isEmpty(sfc);
            sfc.accept(new SFCVisitorChecks());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /** Getter for property args.
     * @return Value of property args.
     */
    public String[] getArgs() {
        return args;
    }
    
    /** Setter for property args.
     * @param args New value of property args.
     */
    public void setArgs(String[] args) {
        this.args = args;
    }
    
}
////////////////////////////////////////////////////////////////
//
//    $Log: not supported by cvs2svn $
//    Revision 1.3  2002/06/19 20:14:16  swprakt
//    Checks -> SFCVisitorChecks [Thomas Richter]
//    isEmpty [Thomas Richter]
//
//    Revision 1.2  2002/06/19 12:18:26  swprakt
//    *** empty log message ***
//
//    Revision 1.1  2002/06/19 11:11:32  swprakt
//    initial
//
//
////////////////////////////////////////////////////////////////