
package slime.absynt.absfc;
import java.io.Serializable;


/**
 * Statements for (abstract SFC tree).
 * The statements are implemented as abstract class, with
 * the concrete classes as subclasses.
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: Statement.java,v 1.0 2002/06/12 19:51:56 mwe
 */

public abstract class Statement extends Absfc implements Serializable { 
    public int elemNr; // number of Statement in a Statementlist
}
// ---------------------------------------------------------------------
