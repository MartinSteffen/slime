package slime.absynt.absfc;
import java.io.Serializable;

/**
 * Abstract class to provide depth and processed.
 *
 * The classes of this package
 * are an implemementation of an abstract SFC tree
 *
 * @author initially provided by Marco Wendel.
 * @version  $Id: Absfc.java,v 1.2 2002-06-25 05:25:02 swprakt Exp $
 */

abstract public class Absfc implements Serializable {
    public int depth = 0; // is set during processing sfcabtree to sfc
    public boolean processed = false; // is set if statement has been converted to steps and transitions
    public boolean hascontent = true; // is set false if std. constructor is called 
    public String nodetype; // good for serialization

    /** for easier processing of the SFCabtree */
    public slime.absynt.Step start_step; /** start step within sfc */
    public slime.absynt.Step end_step; /** end step within sfc */
    public slime.absynt.Transition stmt_trans; /** currently useless transition */
}
// ---------------------------------------------------------------------


