package slime.simulator;
import  java.util.LinkedList;


/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Transitions{
  private Transition[] transitions;

  Transitions(LinkedList trans){
    transitions = new Transition[trans.size()];
	for (int i = 0; i < trans.size() ; i++)
      transitions[i] = new Transition((slime.absynt.Transition) trans.get(i));
  }

  protected String[] getPossible(State state, String[] steps){
    String[] result;
    int      size   = 0;
    int      j      = 0;

    for (int i = 0; i < transitions.length ; i++){
      if (transitions[i].isPossible(state, steps)){
        size++;
      }
    }
    result = new String[size];
    for (int i = 0; i < transitions.length ; i++){
      if (transitions[i].isPossible(state, steps)){
        result[j] = transitions[i].toString();
        j++;
      }
    }

    return result;
  }

  protected void perform(String trans, Steps steps){
    for (int i = 0; i < transitions.length ; i++){
      transitions[i].perform(trans, steps);
    }
  };

  public String toString(){
    String result = "";

    for (int i = 0; i < transitions.length ; i++){
      result = result + transitions[i].toString() + "\n";
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

