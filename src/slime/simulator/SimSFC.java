package slime.simulator;
import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;
import  slime.absynt.SFC;



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: SimSFC.java,v 1.3 2002-07-19 09:04:58 swprakt Exp $
 */


public class SimSFC{
  public static final int ACTIONS     = 0;
  public static final int TRANSITIONS = 1;

  private String      initialStep;
  private Steps       steps;
  private Transitions transitions;
  private Components  actions;
  private State       state;
  private int         phase;

  SimSFC(SFC sfc){
    initialStep = sfc.istep.name;
    steps       = new Steps(sfc.steps);
    transitions = new Transitions(sfc.transs);
    actions     = new Components(sfc.actions);
    state       = new State(sfc.declist);
  }

  protected JScrollPane createStatePane(Simulator master){
    return state.createStatePane(master);
  }

  protected void start(){
    steps.init(initialStep);
    actions.activate(steps.getActiveActions());
    state.init();
    phase = ACTIONS;
  }

  protected void perform(String step){
    if (phase == ACTIONS)
      actions.perform(step, state);
    else
      transitions.perform(step, steps);
  }

  protected void changePhase(){
    if (phase == ACTIONS){
      phase = TRANSITIONS;
    } else {
      steps.activate();
      actions.activate(steps.getActiveActions());
      phase = ACTIONS;
    }
  }

  protected void read(){
    state.read();
  }

  protected String[] getPossibleActions(){
    String[] result = new String[0];

    if (phase == ACTIONS){
      result = actions.getPossible();
    } else {
      result = transitions.getPossible(state, steps.getActive());
    }

    return result;
  }

  public String toString(){
    String result;

    result =          "SFC" + "\n";
    result = result + "Init: " + initialStep + "\n";
    result = result + "Steps: " + "\n";
    result = result + steps.detailsToString();
    result = result + "Transitions: " + "\n";
    result = result + transitions.toString();
    result = result + "Actions: " + "\n";
    result = result + actions.toString();
    result = result + "State: " + "\n";
    result = result + state.toString();

    return result;
  }
}




//----------------------------------------------------------------------
//  Abstract syntax for Slime programs
//  ------------------------------------
//
//  $Id: <dollar>
//
//  $Log: <dollar>
//
//---------------------------------------------------------------------

