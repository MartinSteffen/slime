package slime.simulator;
import  slime.absynt.BoolType;
import  slime.absynt.IntType;
import  slime.absynt.Expr;
import  slime.absynt.B_expr;
import  slime.absynt.U_expr;
import  slime.absynt.Variable;
import  slime.absynt.Constval;


/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


abstract class Expression{
  protected abstract Object eval(State state);

  protected static Expression translate(Expr expression){
	Expression result = new NumericValue(0);

    if (expression instanceof Constval)
      if (((Constval) expression).val instanceof Boolean)
        result = new BooleanValue(((Boolean) ((Constval) expression).val).booleanValue());
      else
        result = new NumericValue(((Integer) ((Constval) expression).val).intValue());
    else if (expression instanceof Variable)
      if (((Variable) expression).type instanceof BoolType)
        result = new BooleanVariable(((Variable) expression).name);
      else
        result = new NumericVariable(((Variable) expression).name);
    else if (expression instanceof U_expr)
	  result = new Negation((BooleanExpression) translate(((U_expr) expression).sub_expr));
	else {
	  B_expr expr = (B_expr) expression;
	  switch (expr.op){
		case Expr.AND    : result = new Conjunction(Conjunction.AND,
		                                           (BooleanExpression) translate(expr.left_expr),
		                                           (BooleanExpression) translate(expr.right_expr)); break;
		case Expr.OR     : result = new Conjunction(Conjunction.OR,
		                                           (BooleanExpression) translate(expr.left_expr),
		                                           (BooleanExpression) translate(expr.right_expr)); break;
		case Expr.EQ     : result = new Comparison(Comparison.EQ,
		                                          (ArithmeticExpression) translate(expr.left_expr),
		                                          (ArithmeticExpression) translate(expr.right_expr)); break;
		case Expr.LESS   : result = new Comparison(Comparison.LESS,
		                                          (ArithmeticExpression) translate(expr.left_expr),
		                                          (ArithmeticExpression) translate(expr.right_expr)); break;
		case Expr.GREATER: result = new Comparison(Comparison.GREATER,
		                                          (ArithmeticExpression) translate(expr.left_expr),
		                                          (ArithmeticExpression) translate(expr.right_expr)); break;
		case Expr.LEQ    : result = new Comparison(Comparison.LEQ,
		                                          (ArithmeticExpression) translate(expr.left_expr),
		                                          (ArithmeticExpression) translate(expr.right_expr)); break;
		case Expr.GEQ    : result = new Comparison(Comparison.GEQ,
		                                          (ArithmeticExpression) translate(expr.left_expr),
		                                          (ArithmeticExpression) translate(expr.right_expr)); break;
		case Expr.NEQ    : result = new Comparison(Comparison.NEQ,
		                                          (ArithmeticExpression) translate(expr.left_expr),
                                                  (ArithmeticExpression) translate(expr.right_expr)); break;
	    case Expr.PLUS : result = new ArithmeticOperation(ArithmeticOperation.PLUS,
	                                                     (ArithmeticExpression) translate(expr.left_expr),
	                                                     (ArithmeticExpression) translate(expr.right_expr)); break;
	    case Expr.MINUS: result = new ArithmeticOperation(ArithmeticOperation.MINUS,
	                                                     (ArithmeticExpression) translate(expr.left_expr),
	                                                     (ArithmeticExpression) translate(expr.right_expr)); break;
	    case Expr.TIMES: result = new ArithmeticOperation(ArithmeticOperation.TIMES,
	                                                     (ArithmeticExpression) translate(expr.left_expr),
	                                                     (ArithmeticExpression) translate(expr.right_expr)); break;
	    case Expr.DIV  : result = new ArithmeticOperation(ArithmeticOperation.DIV,
	                                                     (ArithmeticExpression) translate(expr.left_expr),
	                                                     (ArithmeticExpression) translate(expr.right_expr)); break;
	    default        : result = new NumericValue(0);
      }
    }

	return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */



abstract class ArithmeticExpression extends Expression {
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class ArithmeticOperation extends ArithmeticExpression {
  protected static final int PLUS  = 0;
  protected static final int MINUS = 1;
  protected static final int TIMES = 2;
  protected static final int DIV   = 3;

  private   int                  operation;
  private   ArithmeticExpression operand1;
  private   ArithmeticExpression operand2;

  ArithmeticOperation(int oper, ArithmeticExpression op1, ArithmeticExpression op2){
    operation = oper;
    operand1  = op1;
    operand2  = op2;
  }

  protected Object eval(State state){
    int result = 0;

    switch (operation){
      case 0: result = ((Integer) operand1.eval(state)).intValue() + ((Integer) operand2.eval(state)).intValue(); break;
      case 1: result = ((Integer) operand1.eval(state)).intValue() - ((Integer) operand2.eval(state)).intValue(); break;
      case 2: result = ((Integer) operand1.eval(state)).intValue() * ((Integer) operand2.eval(state)).intValue(); break;
      case 3: result = ((Integer) operand1.eval(state)).intValue() / ((Integer) operand2.eval(state)).intValue(); break;
    }

    return new Integer(result);
  }

  public String toString(){
    String result;
    String opSymbol = "";

    switch (operation){
      case 0: opSymbol = "+"; break;
      case 1: opSymbol = "-"; break;
      case 2: opSymbol = "*"; break;
      case 3: opSymbol = "/"; break;
    }
    result = "(" + operand1.toString() + opSymbol + operand2.toString() + ")";

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class NumericVariable extends ArithmeticExpression{
  private String  name;

  NumericVariable(String nam){
    name  = nam;
  }

  protected Object eval(State state){
    return (Integer) state.get(name);
  }

  public String toString(){
    return name;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */

class NumericValue extends ArithmeticExpression {
  private Integer value;

  NumericValue(int val){
    value = new Integer(val);
  }

  protected Object eval(State state){
    return value;
  }

  public String toString(){
    return "" + value;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


abstract class BooleanExpression extends Expression {
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class Comparison extends BooleanExpression {
  protected static final int LESS    = 0;
  protected static final int GREATER = 1;
  protected static final int LEQ     = 2;
  protected static final int GEQ     = 3;
  protected static final int EQ      = 4;
  protected static final int NEQ     = 5;

  private   int                  operation;
  private   ArithmeticExpression operand1;
  private   ArithmeticExpression operand2;

  Comparison(int oper, ArithmeticExpression op1, ArithmeticExpression op2){
    operation = oper;
    operand1  = op1;
    operand2  = op2;
  }

  protected Object eval(State state){
    boolean result = false;

    switch (operation){
      case 0: result = ((Integer) operand1.eval(state)).intValue() <  ((Integer) operand2.eval(state)).intValue(); break;
      case 1: result = ((Integer) operand1.eval(state)).intValue() >  ((Integer) operand2.eval(state)).intValue(); break;
      case 2: result = ((Integer) operand1.eval(state)).intValue() <= ((Integer) operand2.eval(state)).intValue(); break;
      case 3: result = ((Integer) operand1.eval(state)).intValue() >= ((Integer) operand2.eval(state)).intValue(); break;
      case 4: result = ((Integer) operand1.eval(state)).intValue() == ((Integer) operand2.eval(state)).intValue(); break;
      case 5: result = ((Integer) operand1.eval(state)).intValue() != ((Integer) operand2.eval(state)).intValue(); break;
    }

    return new Boolean(result);
  }


  public String toString(){
    String result;
    String opSymbol = "";

    switch (operation){
      case 0: opSymbol = "<"; break;
      case 1: opSymbol = ">"; break;
      case 2: opSymbol = "<="; break;
      case 3: opSymbol = ">="; break;
      case 4: opSymbol = "=="; break;
      case 5: opSymbol = "!="; break;
    }
    result = "(" + operand1.toString() + opSymbol + operand2.toString() + ")";

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class Conjunction extends BooleanExpression {
  protected static final int AND = 0;
  protected static final int OR  = 1;

  private   int               operation;
  private   BooleanExpression operand1;
  private   BooleanExpression operand2;

  Conjunction(int oper, BooleanExpression op1, BooleanExpression op2){
    operation = oper;
    operand1  = op1;
    operand2  = op2;
  }

  protected Object eval(State state){
    boolean result = false;

    switch (operation){
      case 0: result = ((Boolean) operand1.eval(state)).booleanValue() && ((Boolean) operand2.eval(state)).booleanValue(); break;
      case 1: result = ((Boolean) operand1.eval(state)).booleanValue() || ((Boolean) operand2.eval(state)).booleanValue(); break;
    }

    return new Boolean(result);
  }

  public String toString(){
    String result;
    String opSymbol = "";

    switch (operation){
      case 0: opSymbol = " and "; break;
      case 1: opSymbol = " or "; break;
    }
    result = "(" + operand1.toString() + opSymbol + operand2.toString() + ")";

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class Negation extends BooleanExpression {
  private BooleanExpression operand;

  Negation(BooleanExpression op){
    operand = op;
  }

  protected Object eval(State state){
    boolean result;

    result = !((Boolean) operand.eval(state)).booleanValue();

    return new Boolean(result);
  }

  public String toString(){
    String result;

    result = "not " + operand;

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class BooleanVariable extends BooleanExpression{
  private String  name;

  BooleanVariable(String nam){
    name  = nam;
  }

  protected Object eval(State state){
    return (Boolean) state.get(name);
  }

  public String toString(){
    return name;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Expression.java,v 1.2 2002-07-19 08:00:51 swprakt Exp $
 */


class BooleanValue extends BooleanExpression {
  private Boolean value;

  BooleanValue(boolean val){
    value = new Boolean(val);
  }

  protected Object eval(State state){
    return value;
  }

  public String toString(){
    return "" + value;
  }
}
