
package slime.checks;
import java.lang.*;
import java.util.*;
import slime.absynt.*;






/** checking well-formedness for Slime programs
 *
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: Wellformed.java,v 1.6 2002-07-09 12:58:25 swprakt Exp $
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
   *  General, non-specific exception when doing wellformed checking.
   *  The exception is subclassed by various specific, concrete
   *  exceptions and the checker never throws the the general one.
   */

  public class WException extends CheckException { // Exception as name not possible
    String message = "general well-formedness exception";    

    public WException () {};

    public WException (String m) {
      message = m;}
    
    public String getMessage(){return message;  }
  }




  public boolean check (SFC s) throws WException { 
    if (s == null)
      throw (new WException("sfc is empty")); 
    (new Initcheck()).check(s);
    (new Consistency()).check(s);
    return true;}




  /** Visitor for SFC's, the entry point of the recursion.
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
			   LinkedList steps, // list of steps
			   LinkedList transs,
			   LinkedList actions, 
			   LinkedList declist) throws Exception {
	// trickle down the list to see whether there
	// it contains a step with identical name.
	for (Iterator i = steps.iterator(); i.hasNext(); ) {
	  Step s  = (Step)i.next();
	  if (s.equals(istep))
	    return (new Boolean(true)); // exit loop
	}
	throw new Wellformed.WException();
      }
    }
  }

  /** Inner class for consistency checking. Encapsulates and 
   *  groups together the  visitor classes that recur down the sfc structure.
   *
   */

  public class Consistency {
    slime.utils.PrettyPrint             pp = new slime.utils.PrettyPrint(); //temporary
    private HashMap steptable = new HashMap();    // hash table for steps

    public boolean check (SFC s)  throws Wellformed.WException {
      try {
	Boolean  b = (Boolean)(s.accept(new SFCV()));
	return b.booleanValue();
      }
      catch (Exception ex) {
	throw (Wellformed.WException)(ex);}
    }

    /** SFC-visitor for consistency. currently stub.
     *  The checker guarantes the following properties.
     *  The steps are unique, and each step mentionend in
     *  a transition is listed in the set of steps.
     *  In all cases, steps are identified up-to their
     *  name, not the instance identity.
     */
    class SFCV implements Visitors.ISFC{
      public Object forSFC(Step istep, 
			   LinkedList steps, // list of steps
			   LinkedList transs,
			   LinkedList actions, 
			   LinkedList declist) throws Exception {
	for (Iterator i = steps.iterator(); i.hasNext(); ){
	  Step s = (Step)i.next();
	  pp.print(s);
	  if (steptable.containsKey(s.name))
	    throw new WException("duplicated step, named: " + s.name);  
	  steptable.put(s.name, null);
	}
	return (new Boolean(true)); // stub
      }
    }
    
    /** consistency  visitor for a step.
     */
    
//      class StepV implements Visitors.IStep{
//        public Object forStep(String name, LinkedList actions) throws Exception {
//  	for (Iterator i = actions.iterator(); i.hasNext(); ) {
//  	  StepAction sa  = (StepAction)i.next();
//  	sa.accept(new StepActionV()); 
//  	}
//  	return new UnitType();
//        }
//      }


    
  } // end of Consistency



}



//----------------------------------------------------------------------
//    well-formed analysis  for Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.5  2002/07/08 10:25:27  swprakt
//    Stub code for consistency checking added.
//    [Steffen]
//
//    Revision 1.4  2002/07/08 09:01:32  swprakt
//    *** empty log message ***
//
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
