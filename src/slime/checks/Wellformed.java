
package slime.checks;
import java.lang.*;
import java.util.*;
import slime.absynt.*;






/** checking well-formedness for Slime programs
 *
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: Wellformed.java,v 1.4 2002-07-08 09:01:32 swprakt Exp $
 * <p>
 * The checker consists of various well-formed errors as exceptions together with the
 * checker proper, which recurs over the abstract syntax.
 * 
 * Using internally the visitor pattern, the design is 
 * similar to the one of the type checker. cf. there for further info.
 * <p>
 * Given an sfc, the following things are checked:
 * <ul>
 *  <li> sfc non-nil
 *  <li> initial step contained in the set of steps.
 * </ul>
 *
 */
public class Wellformed {

  /* Next the list of exceptions */

  /**
   *  General, non-specific exception when doing type checking.
   *  The exception is subclassed by various specific, concrete
   *  exceptions and the checker never throws the the general one.
   */

  public class WException extends CheckException {
    String message = "general well-formedness exception";    
    public String getMessage(){return message;  }
  }




  public boolean check (SFC s) throws WException { 
    return true;}




  /** Type check visitor for SFC's, the entry point of the recursion.
   *  To cope with contextual information, we use a  hash table.
   *  The key objects are the variables, the values are the types.
   *
   */


  

  public class Initcheck {
    public boolean check (SFC s)  throws Wellformed.WException {
      try {
	Boolean  b = (Boolean)(s.accept(new SFCV()));
	return b.booleanValue();
      }
      catch (Exception ex) {
	throw (Wellformed.WException)(ex);}
    }
    
    class SFCV implements Visitors.ISFC{
      public Object forSFC(Step istep, 
			   LinkedList steps,
			   LinkedList transs,
			   LinkedList actions, 
			   LinkedList declist) throws Exception {
	return (new Boolean(true));
      }
    }
  }

}



//----------------------------------------------------------------------
//    well-formed analysis  for Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.3  2002/07/07 14:57:46  swprakt
//    ok for today [Steffen]
//
//    Revision 1.2  2002/07/05 18:35:22  swprakt
//    currently still empty [Steffen]
//
//    Revision 1.1  2002/07/05 18:34:06  swprakt
//    *** empty log message ***
//
//
//---------------------------------------------------------------------
