
package slime.checks;

import java.lang.*;
import java.util.*;
import slime.absynt.*;




/** Type checker for Slime programs
 *
 * <p>
 * It consists of the various typecheck errors together with the
 * type checker proper, which recurs over the abstract syntax.
 *
 * <P>The type checker is rather simple, in the first stage it does not
 * attempt to infer types.  As a consequence, the type checking is
 * context-free, because vor variables we need not look up the type from
 * some environment. It is implemented using the visitor
 * pattern.</P>
 * <P>The class typecheck contains as inner classes
 * the various cases. For each syntactic category, the visitor
 * class has the matching name, suffixed  with a ``V'', for instance
 * ExprV for the visitor of absynt.Expr. Note that it is not
 * possible to give it the same name.</P>
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version $Id: Typecheck.java,v 1.24 2002-07-05 16:50:54 swprakt Exp $
 */

public class Typecheck {
  
  /* I would have been nicer, perhaps, to put all the exceptions
   * as pulic classes of an external class. Java does not allow this;
   * the declaration of the exceptions must ``enclose''
   * the place where the can be thrown, here the  check-routines below.
   */




  /**
   *  The environment for type checking contains the association
   *  of variables to types. It is a wrapper around the hash table from
   * the library.  Unlike as for hash tables, we don't allow duplicated
   * entries.
   */

  public class TEnv {
    // Hashmap is more efficient that Hashtable (but not thread safe)
    private HashMap e = new HashMap();  
    public void add (Variable x, Type t) throws DuplicatedBinding { 
      if (e.containsKey(x))
	throw new DuplicatedBinding();
      e.put(x,t);}

    public Type lookup (Variable x)  {
      return (Type)(e.get(x));}
  };

  TEnv env  = new TEnv();  // initially emtpy

  /* Next the list of exceptions */
  public class TypecheckException extends CheckException {
    String message = "general typecheck exception";    
    public String getMessage(){return message;  }
  }


  private class DuplicatedBinding extends Exception { // not for users
    String message = "Environments assume unique bindings";
    public String getMessage(){return message;  }
  }


  public class  DuplicatedDeclaration extends TypecheckException {
    String message = "Variable bound twice.";
    String explanation = "No variable must be bound more than once";
    public String getMessage(){return message;  }
  }

  public  class NonuniqueDeclaration extends TypecheckException {
    String explanation = "No variable may occur twice int the declaration list";  
    public String getMessage(){return message;  }
  }
    
  public class UnbooleanGuard extends TypecheckException {
    String explanation = "The guard of a transition must be an expression\n of boolean type. This error indicates the occurrence of a\n well-typed guard-expression, but with a type other than a boolean." ;
    public String getMessage(){return message;  }
  }
    
  public class UndeclaredVariable extends TypecheckException{
    String explanation = "Each variable occuring int a program must be\n covered by a type declaration for this variable where the declaration\n is unique";
    public String getMessage(){return message;  }
    }


  public  class IncompleteDeclaration extends TypecheckException{
    String explanation = "A variable declation consists of three parts: \n 1) variable name, 2) a type, and 3), a constant value.\n If one of them is the missing (i.e., nil), this error is raised.";
    public String getMessage(){return message;  }
  }

  public class NoUsertype extends TypecheckException{
    String message = "Not a user type";
    String explanation = "The type is not allowed for source programs, it's for\n type checker internal use, only."; 
    public String getMessage(){return message;  }
  }

  public class TypeMismatch extends TypecheckException{
    String message = "Type mismatch";
    String explanation = " A type mismatch can occur in various situations: in compund expressions, for instance int ``1+true''. \n Furthermore, in an assigment, the type of the expression on the \n right-hand side must coincide with the declared type of the variable. The same restriction\n holds for the variables and the initial value int a type declaration.";
    public String getMessage(){return message;  }
  }

  /** Type check cisitor for SFC's, the entry point of the recursion.
   *  To cope with contextual information, we use a  hash table.
   *  The key objects are the variables, the values are the types.
   *
   */
  public class SFCV implements Visitors.ISFC{
    public Object forSFC(Step istep, 
			 LinkedList steps,
			 LinkedList transs,
			 LinkedList actions, 
			 LinkedList declist) throws Exception {
      istep.accept(new StepV());
      // each step int the list of steps must be well-typed
      // if one of the single steps fails, it will raise an error. 
      for (Iterator i = steps.iterator(); i.hasNext(); ) {
 	Step s = (Step)i.next();
	s.accept(new StepV());}
      for (Iterator i = transs.iterator(); i.hasNext(); ) {
 	Transition t = (Transition)i.next();
	t.accept(new TransitionV()); 
      }
      for (Iterator i = transs.iterator(); i.hasNext(); ) {
 	Action a = (Action)i.next();
	a.accept(new ActionV()); 
      }
      for (Iterator i = transs.iterator(); i.hasNext(); ) {
 	Declaration d  = (Declaration)i.next();
	d.accept(new DeclarationV()); 
	try {
	  env.add(d.var, d.type); // 1: key, 2: value
	}
	catch (DuplicatedBinding e) {
	  throw new DuplicatedDeclaration(); 
	}
      }
      return new UnitType();
    }
  }

  /** Type checking visitor for a step.
   *  A step has no value, only side-effects, i.e., type checking a step
   *  either returns UnitType or raises an error.
   */

  public class StepV implements Visitors.IStep{
    public Object forStep(String name, LinkedList actions) throws Exception {
      for (Iterator i = actions.iterator(); i.hasNext(); ) {
 	StepAction sa  = (StepAction)i.next();
	sa.accept(new StepActionV()); 
      }
      return new UnitType();
    }
  }

  /** Type checking visitor for a step action.
   *  A step action is always well-typed (as also the action
   *  qualifier deeper int the recursion is). Nevertheless, we 
   *  implement it as visitor for uniformity and extensibility.
   */
  public class StepActionV implements Visitors.IStepAction{
    public Object forStepAction(ActionQualifier qualifier,
				String a_name) throws Exception {
      qualifier.accept(new ActionQualifierV());
      return new UnitType();
    }
  }

  /** Type checking visitor for actions. An action does not return
   *  a value, so it has type UnitType, when well-typed.
   *  In our language and type system, each statement int
   *  the sequence is required to be of unit type. As it
   *  is an invariant, that a statement's type is indeed
   *  a unit type (or else an exception is raised), we
   *  don't check this in the iteration again.
   */

  public class ActionV implements Visitors.IAction{
    public Object forAction(String a_name, LinkedList sap) throws Exception{
      for (Iterator i = sap.iterator(); i.hasNext(); ) {
 	Stmt sa  = (Stmt)i.next();
	sa.accept(new StmtV()); 
      }
      return new UnitType();
    }
  }

  /** type checking visitor for statements.
   *  A statement does not return a value.
   */
  public class StmtV implements Visitors.IStmt{
    public Object forSkip() throws Exception {
      return new UnitType();
    }
    public Object forAssign(Variable x, Expr e) throws Exception {
      // XXX add compatibility check of x and e
      Type t_x = (Type)x.accept(new ExprV()); // type of x
      Type t_e = (Type)e.accept(new ExprV()); // type of e
      if (!(t_x.equals(t_e)))
	throw new TypeMismatch();
      return new UnitType();
    }
  }

  /** type checking visitor for expressions.
   */


  public slime.absynt.Type checkexprxxx (Expr e)  throws CheckException {
    try {
      slime.absynt.Type t = (slime.absynt.Type)(e.accept(new ExprV()));
      return t;
    }
    catch (Exception ex) {
      throw (CheckException)(ex);}
  }

  public class ExprV implements Visitors.IExpr{
        
    public Object forB_Expr(Expr l, int o, Expr r) throws CheckException {
            // binary expressions
            try {
                Type t_l = (Type)(l.accept(this));
                Type t_r = (Type)(r.accept(this));
                if ((o == Expr.LEQ) | (o == Expr.GEQ) | (o == Expr.LESS) | (o == Expr.GREATER)) {
                    if ((t_l instanceof IntType) && (t_r instanceof IntType))
                        return new BoolType();
                    else throw new TypeMismatch();
                }
                else if  ((o == Expr.PLUS) || (o == Expr.MINUS) || (o == Expr.TIMES) || (o == Expr.DIV)) {
                    if ((t_l instanceof IntType) && (t_r instanceof IntType))
                        return new IntType();
                    else throw new TypeMismatch();
                }
                else if ((o == Expr.AND) || (o == Expr.OR)) {
		  System.out.println ("andor");
		  if ((t_l instanceof BoolType) && (t_r instanceof BoolType))
		    return new BoolType();
		  else throw new TypeMismatch();
                }
		else if ((o == Expr.EQ) || (o == Expr.NEQ)) {
                    if (((t_l instanceof IntType) && (t_r instanceof IntType)) ||
                    ((t_l instanceof BoolType) && (t_r instanceof BoolType)))
                        return new BoolType();
                    else throw new TypeMismatch();
                }
                else
                    throw new TypeMismatch();
            }
            catch (Exception e) {
	      System.out.println("here we go");
	      System.out.println(e.getMessage());
	      throw (TypeMismatch)(e);
            }
            // this last try-catch-throw is a type-cast for
            // exceptions. The visitor is declared to throw the most
            // general exception, but here, in the check package,
            // it is better to be specific.  Since for instance  l.accept(this)
            // is only known to throw a general ``Exception'', the type checker
            // complains if we don't wrap up the method int a try-catch statement
            // and hand give back the more concrete exception.
        }
    public Object forU_Expr(Expr e, int o) throws CheckException {
      /**
       * Unary expressions are negations on booleans and minus on integers
       */
      
      try {
	Type t = (Type)(e.accept(this));   // we have to cast, that part of it.
	if     ((t instanceof BoolType) &&  (o == Expr.NEG)) return t;
	else if ((t instanceof IntType) &&  (o == Expr.MINUS)) return t;
	else throw new TypeMismatch();
      }
      catch (Exception e2) {
	throw new TypeMismatch();
      }
    }
        
    public Object forVariable(String s, Type t) throws CheckException {
      Type t_dec = env.lookup(new Variable(s));
      if (t_dec == null)
	throw new UndeclaredVariable();
      return t_dec;
    }
        
    public Object forConstval(Object v) throws CheckException {
      /**
       * Constant values can be integers or booleans.
       */
      if      (v instanceof Boolean) return new BoolType();
      else if (v instanceof Integer) return new IntType();
      else throw new CheckException();
    }
  }

  /** Type checking visitor for action qualified.
   *  an action qualifier is always well-typed.
   */
  public class ActionQualifierV implements Visitors.IActionQualifier{
    public Object forNqual(){return new UnitType();}
  }


    
    // --------------------------
    
    /** Visitor for transitions
     */
  
  public class TransitionV implements Visitors.ITransition{
    public Object forTransition(LinkedList source,
				Expr guard,
				LinkedList target) throws CheckException {
      try {
	Type t = (Type)guard.accept(new ExprV());
	if (!(t instanceof BoolType)) // guards must be boolean
	  throw new UnbooleanGuard();
	// source and targets are linked lists of steps.
	// they must iterated through and checked for well-typedness.
	for (Iterator i = source.iterator(); i.hasNext(); ) {
	  Step s  = (Step)i.next();
	  s.accept(new StepV()); 
	}
	for (Iterator i = target.iterator(); i.hasNext(); ) {
	  Step s  = (Step)i.next();
	  s.accept(new StepV()); 
	}
	return new UnitType();
      }
      catch (Exception e) {
	throw ((CheckException)e);
      }
    }
  }

  /** Type checking visitor for a declaration.
   *  A declaration must have all three fields filled in, i.e.,
   *  non is allowed to be a nil-reference. Furthermore the type
   *  of the value must coincide with the declared type.
   */
  public class DeclarationV implements Visitors.IDeclaration{
    public Object forDeclaration(Variable var, Type type, Constval val)
      throws CheckException {
      try{
	if ((var == null) || (type == null) || val == null)
	  throw new IncompleteDeclaration();
	Type t_dec = (Type)(type.accept(new TypeV())); // declared type
	if (t_dec instanceof UnitType)
	  throw new NoUsertype();
	Type t_act = (Type)(val.accept(new ExprV()));  // actual type
	if (!(t_dec.equals(t_act)))
	  throw  new TypeMismatch();
	// --- add the binding x:T to the enviroment.
	env.add(var, t_dec);
	return new Object();
      }
      catch (Exception e){
	throw ((CheckException)e);
      }
    }
  }

  /** Type checking visitor for a type itself.
   *  This is the end of the recursion and the visitor returns
   *  the object itself. The only thing that can goto wrong is that
   *  the
   */

  public class TypeV implements Visitors.IType{
    public Object forIntType() { 
      return new IntType();}

    public Object forBoolType() {
      return new BoolType();}

    public Object forUnitType() {
      return new UnitType();}

    public Object forUndefType() {
      return new UndefType();}
  }
}


//----------------------------------------------------------------------
//    checks/static analysis  Slime programs
//    ----------------------------------------
//
//    $Log: not supported by cvs2svn $
//    Revision 1.23  2002/07/05 14:05:32  swprakt
//    *** empty log message ***
//
//    Revision 1.22  2002/07/05 14:01:56  swprakt
//    *** empty log message ***
//
//    Revision 1.21  2002/07/05 06:49:41  swprakt
//    Some more messages. Type mismatch will also be used for expressions.
//
//    Steffen
//
//    Revision 1.20  2002/06/26 18:09:01  swprakt
//    ok for today.
//
//    Steffen
//
//    Revision 1.19  2002/06/26 05:13:58  swprakt
//    *** empty log message ***
//
//    Revision 1.18  2002/06/25 17:30:56  swprakt
//    ok
//
//    Revision 1.17  2002/06/25 17:20:45  swprakt
//    Type check done for transitions.
//
//    [Steffen]
//
//    Revision 1.16  2002/06/25 17:16:11  swprakt
//    type check clause for action is done
//
//    [Steffen]
//
//    Revision 1.15  2002/06/25 17:00:53  swprakt
//    step typechcking done
//
//    [Steffen]
//
//    Revision 1.14  2002/06/25 16:52:26  swprakt
//    all fields of SFC-clause are covered by the recusive
//    visitor structure of the type checker.
//
//    [Steffen]
//
//    Revision 1.13  2002/06/25 10:55:47  swprakt
//    First iterator added for one linked list.
//    [Steffen]
//
//    Revision 1.12  2002/06/25 08:23:38  swprakt
//    Except the linked list and the context, the checker is through.
//
//    [Steffen]
//
//    Revision 1.11  2002/06/25 08:20:10  swprakt
//    typechecking for statements. [Steffen]
//
//    Revision 1.10  2002/06/25 07:52:11  swprakt
//    New visitors added (for actions). [Steffen]
//
//    Revision 1.9  2002/06/25 07:39:19  swprakt
//    acceptor added, typechecker extended for SFC-clause. [Steffen]
//
//    Revision 1.8  2002/06/25 06:17:58  swprakt
//    type checking for action qualifiers and step actions done
//    [Steffen]
//
//    Revision 1.7  2002/06/25 05:22:57  swprakt
//    Type checking (as visitor) for declarations done.
//
//    [Steffen]
//
//    Revision 1.6  2002/06/24 20:08:12  swprakt
//    Types extended with visitor methods,
//    also extended is the type ckecker, which is a visitor to the absynt.
//
//    [Steffen]
//
//    Revision 1.5  2002/06/24 19:14:08  swprakt
//    I removed the class Typeerrors. It's inner classes (the
//    exceptions for typechecking) I put into the class Typcheck.
//    Java does not allow the previously intended setup.
//
//    [Steffen]
//
//    Revision 1.4  2002/06/19 23:00:10  swprakt
//    no message
//
//    Revision 1.3  2002/06/14 16:50:33  swprakt
//    ok, 1 week pause
//
//    Revision 1.2  2002/06/14 06:37:41  swprakt
//    SFC's may be  named, now (as decided 12.6.2002).
//    Corresponding field + constructors added.
//
//    [M. Steffen]
//
//    Revision 1.1  2002/06/13 12:34:28  swprakt
//    Started to add vistors + typechecks [M. Steffen]
//
//    $Id: Typecheck.java,v 1.24 2002-07-05 16:50:54 swprakt Exp $
//
//---------------------------------------------------------------------
