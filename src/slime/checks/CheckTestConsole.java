/*
 * CheckTestConsole.java
 *
 * Created on 19. Juni 2002, 12:53
 */

package slime.checks;

/** a console test tool
 * @author <a href="mailto:richter@thomas-richter.de">Thomas Richter</a>
 * @version $Id: CheckTestConsole.java,v 1.2 2002-06-19 12:18:26 swprakt Exp $
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
//    Revision 1.1  2002/06/19 11:11:32  swprakt
//    initial
//
//
////////////////////////////////////////////////////////////////