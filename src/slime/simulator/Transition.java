package slime.simulator;
import java.io.Serializable;



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Transition implements Serializable {
  private String[]          source;
  private BooleanExpression guard;
  private String[]          target;

  Transition(slime.absynt.Transition trans){
    source = new String[trans.source.size()];
    for (int i = 0; i < trans.source.size() ; i++){
      source[i] = ((slime.absynt.Step) trans.source.get(i)).name;
    }

    guard  = (BooleanExpression) Expression.translate(trans.guard);

    target = new String[trans.target.size()];
    for (int i = 0; i < trans.target.size() ; i++){
      target[i] = ((slime.absynt.Step) trans.target.get(i)).name;
    }
  }

  protected boolean isPossible(State state, String[] steps){
    boolean result = true;

    for (int i = 0; i < source.length ; i++){
      boolean active = false;

      for (int j = 0; j < steps.length ; j++){
        if (steps[j].equals(source[i])){
          active = true;
        }
      }

      result = result && active;
    }

    result = result && ((Boolean) guard.eval(state)).booleanValue();

    return result;
  }

  protected void perform(String trans, Steps steps){
    if (toString().equals(trans)){
      for (int i = 0; i < source.length ; i++){
        steps.deactivate(source[i]);
      }
      for (int i = 0; i < target.length ; i++){
        steps.setReady(target[i]);
      }
    }
  }

  public String toString(){
    String result = "(";

    for (int i = 0; i < source.length ; i++){
      result = result + source[i];
      if (i < source.length-1 ){
        result = result + ";";
      }
    }
    result = result + ") -" + guard.toString() + "-> (";
    for (int i = 0; i < target.length ; i++){
      result = result + target[i];
      if (i < target.length-1 ){
        result = result + ";";
      }
    }
    result = result + ")";

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

