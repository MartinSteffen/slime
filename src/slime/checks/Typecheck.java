package slime.checks;
import java.lang.*;
import slime.absynt.*;

/**
 * Type checker for Slime programs
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version  $Id: Typecheck.java,v 1.1 2002-06-13 12:34:28 swprakt Exp $
 */

/** 
 * The type checker is rather simple, in the first stage it does not
 * attempt to infer types.  As a consequence, the type checking is
 * context-free, because vor variables we need not look up the type from
 * some environment. It is implemented using the visitor
 * pattern. 
 */

public class Typecheck implements Visitors.IExpr{
  public Object forB_Expr(Expr l, int o, Expr r) throws CheckException { // binary expressions
    try {
      Type t_l = (Type)(l.accept(this));
      Type t_r = (Type)(r.accept(this));
      if ((o == Expr.LEQ) | (o == Expr.GEQ) | (o == Expr.LESS) | (o == Expr.GREATER))
	{if ((t_l instanceof IntType) && (t_r instanceof IntType))
	  return new BoolType();
	else throw new CheckException();}
      else if  ((o == Expr.PLUS) || (o == Expr.MINUS) || (o == Expr.TIMES) || (o == Expr.DIV))
	{if ((t_l instanceof IntType) && (t_r instanceof IntType))
	  return new BoolType();
	else throw new CheckException();}
      else if ((o == Expr.EQ) || (o == Expr.NEQ))
	{if (((t_l instanceof IntType) && (t_r instanceof IntType)) ||
	     ((t_l instanceof BoolType) && (t_r instanceof BoolType)))
	  return new BoolType();
	else throw new CheckException();}
      else
	throw new CheckException();
    }
     catch (Exception e) {throw (CheckException)(e);}
    // this last try-catch-throw is a type-cast for
    // exceptions. The visitor is declared to thow the most
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
      else throw new CheckException();}
    catch (Exception e2) {throw new CheckException();}
  }

  public Object forVariable(String s, Type t) { return t;}
  
  public Object forConstval(Object v) throws CheckException {
    /** 
     * Constant values can be integers or booleans.
     */
    if      (v instanceof Boolean) return new BoolType();
    else if (v instanceof Integer) return new IntType();
    else throw new CheckException();}
}









//----------------------------------------------------------------------
//	checks/static analysis  Slime programs
//	----------------------------------------
//
//	$Log: not supported by cvs2svn $
//	$Id: Typecheck.java,v 1.1 2002-06-13 12:34:28 swprakt Exp $
//
//---------------------------------------------------------------------
