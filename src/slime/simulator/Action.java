package slime.simulator;
import  java.util.LinkedList;
import  slime.absynt.Stmt;
import  slime.absynt.BoolType;


/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Action{
  private String  name;
  private Program program;
  private boolean possible;

  Action(slime.absynt.Action act){
    name    = act.a_name;
    program = new Program(act.sap);
  }

  protected String getName(){
    return name;
  }

  protected boolean isPossible(){
    return possible;
  }

  protected void perform(String action, State state){
    if (name.equals(action)){
      program.perform(state);
      possible = false;
    }
  }

  protected void activate(String[] stepActions){
    possible = false;

    for (int i = 0; i < stepActions.length ; i++)
      if (stepActions[i].equals(name))
        possible = true;
  }

  public String toString(){
    String result;

    result = name + "::=" + program.toString();

    return result;
  }
}




/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


abstract class Statement{
  protected abstract void eval(State state);
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Assign extends Statement{
  private String     variable;
  private Expression expression;

  Assign(slime.absynt.Assign assign) {
	variable   = assign.var.name;
    expression = Expression.translate(assign.val);
  }

  protected void eval(State state){
    state.set(variable, expression);
  };

  public String toString(){
    String result;

    result = variable + ":=" + expression.toString();

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Skip extends Statement{
  protected void eval(State start){};

  public String toString(){
    return "skip";
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Program{
  private Statement[] code;

  Program(LinkedList sap){
	code = new Statement[sap.size()];
	for (int i = 0; i < sap.size() ; i++){
	  Stmt      temp         = (Stmt) sap.get(i);
	  Statement newStatement;

	  if (temp instanceof slime.absynt.Assign)
	    newStatement = new Assign((slime.absynt.Assign) temp);
	  else
	    newStatement = new Skip();
      code[i] = newStatement;
    }
  }

  protected void perform(State state){
	for (int i = 0; i < code.length ; i++)
      code[i].eval(state);
  }

  public String toString(){
    String result = "";

    for (int i = 0; i < code.length ; i++){
	  result = result + code[i].toString();
	  if (i < code.length-1)
	    result = result + ";";
    }

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Components{
  private Action[] actions;

  Components(LinkedList actlist){
    actions = new Action[actlist.size()];
    for (int i = 0; i < actlist.size() ; i++)
      actions[i] = new Action((slime.absynt.Action) actlist.get(i));
  }

  protected boolean isEmpty(){
    boolean result = false;

    if (actions.length == 0){
      result = true;
    }

    return result;
  }

  protected void perform(String action, State state){
    for (int i = 0; i < actions.length ; i++){
      actions[i].perform(action, state);
    }
  }

  protected void activate(String[] stepActions){
    for (int i = 0; i < actions.length ; i++){
      actions[i].activate(stepActions);
    }
  }

  protected String[] getPossible(){
    String[] result;
    int      size   = 0;
    int      j      = 0;

    for (int i = 0; i < actions.length ; i++){
      if (actions[i].isPossible()){
        size++;
      }
    }
    result = new String[size];
    for (int i = 0; i < actions.length ; i++){
      if (actions[i].isPossible()){
        result[j] = actions[i].getName();
        j++;
      }
    }

    return result;
  }

  public String toString(){
    String result ="";

    for (int i = 0; i < actions.length ; i++){
      result = result + actions[i].toString() + "\n";
    }

    return result;
  }
}





//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: <dollar>
//
//	$Log: <dollar>
//
//---------------------------------------------------------------------

