
package slime.checks;
import java.lang.*;
import java.util.*;
import slime.absynt.*;






/** checking well-formedness for Slime programs
 *
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: Wellformed.java,v 1.13 2002-07-11 06:24:40 swprakt Exp $
 * <p>
 * The checker consists of various well-formed errors (combined into one exception) together with the
 * checker proper, which recurs over the abstract syntax.
 * 
 * Using internally the visitor pattern, the design is 
 * similar to the one of the type checker. cf. there for further info.
 * <p>
 * Given an sfc, the following things are checked:
 * <ul>
 *  <li> sfc non-null
 *  <li> initial step contained in the set of steps.
 *  <li> all steps and transitions are non-null.
 *  <li> no step occurs twice (by name)
 *  <li> no transition has a non-existing step as source or target (by name)
 *  <li> no transition has at the same time more that one source and
 * more than one target.
 * </ul>
 * Note that test of non-nullness is not done for parts other than mentioned (for instance
 * sap's etc.)
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
    private HashSet stepset  = new HashSet();    // set of steps (resp. their names)

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
	  if (s == null) throw new WException("null-step");
	  pp.print(s);
	  if (stepset.contains(s.name))
	    throw new WException("duplicated step, named: " + s.name);  
	  stepset.add(s.name);
	}

	for (Iterator i = transs.iterator(); i.hasNext(); ){
	  Transition  t = (Transition)i.next();
	  if (t==null) throw new WException("null transition");
	  pp.print(t);
	  t.accept(new TransitionV()); 
	}
	return (new Boolean(true)); // stub
      }
    }

    /** Well-formed visitor for transitions.
     * It checks, whether all source and target steps are indeed
     * listed in the previously collected set of steps.
     */
    class TransitionV implements Visitors.ITransition{
      public Object forTransition(LinkedList source,
				  Expr guard,
				  LinkedList target) throws WException {
	if ((source == null) || (target == null))
	  throw new WException("null source or target in transition.");
	if (source.isEmpty() || target.isEmpty())
	  throw new WException ("no source or no target step in transition.");
	try {
	  for (Iterator i = source.iterator(); i.hasNext(); ) {
	    Step s  = (Step)i.next();
	    s.accept(new StepV()); 
	  }
	  for (Iterator i = target.iterator(); i.hasNext(); ) {
	    Step s  = (Step)i.next();
	    s.accept(new StepV()); 
	  }
	  return (new Boolean(true));
	}
	catch (Exception e) {
	  throw ((WException)e);
	}
      }
    }
    
    /** consistency  visitor for one step. This means, checking 
     *  whether the step (i.e., its name) has been defined.
     */
    
      class StepV implements Visitors.IStep{
        public Object forStep(String name, LinkedList actions) throws Exception {
	  if (!(stepset.contains(name)))
	    throw new WException("undefined step as source/target of a transition");
	  return new Boolean(true);
        }
      }


    
  } // end of Consistency



}



//----------------------------------------------------------------------
//    well-formed analysis  for Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.12  2002/07/11 06:05:48  swprakt
//    *** empty log message ***
//
//    Revision 1.11  2002/07/09 16:06:45  swprakt
//    OK
//
//    Revision 1.10  2002/07/09 14:49:08  swprakt
//    *** empty log message ***
//
//    Revision 1.9  2002/07/09 14:28:40  swprakt
//    Well-formed checked finished. (but untested).  The functionality is as
//    documented in java-doc. It might be that the implementation requires
//    to re-add the equals-methods into the abstract syntax (which has
//    been removed due to the current interface inconsistencies.) This will
//    be tested.
//
//    [Steffen]
//
//    Revision 1.8  2002/07/09 13:51:56  swprakt
//    I check for uniqueness of steps now, where the steps
//    are put into a Set (implemented by a hashtable).
//
//    [Steffen]
//
//    Revision 1.7  2002/07/09 13:00:24  swprakt
//    *** empty log message ***
//
//    Revision 1.6  2002/07/09 12:58:25  swprakt
//    *** empty log message ***
//
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
