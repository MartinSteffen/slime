package slime.simulator;
import  java.util.LinkedList;



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Steps.java,v 1.2 2002-07-19 09:09:08 swprakt Exp $
 */


class Steps{
  private Step[] steps;

  Steps(LinkedList steplist){
    steps = new Step[steplist.size()];
    for (int i = 0; i < steplist.size() ; i++)
      steps[i] = new Step((slime.absynt.Step) steplist.get(i));
  }

  protected void init(String initialStep){
    for (int i = 0; i < steps.length ; i++)
      steps[i].init(initialStep);
  }

  protected void setReady(String step){
    for (int i = 0; i < steps.length ; i++)
      steps[i].setReady(step);
  }

  protected void deactivate(String step){
    for (int i = 0; i < steps.length ; i++)
      steps[i].deactivate(step);
  }

  protected void activate(){
    for (int i = 0; i < steps.length ; i++){
      steps[i].activate();
    }
  }

  protected String[] getActiveActions(){
    String[] result;
    int      size   = 0;
    int      j      = 0;

    for (int i = 0; i < steps.length ; i++)
      if (steps[i].isActive())
        size = size + steps[i].getActions().length;
    result = new String[size];
    for (int i = 0; i < steps.length ; i++)
      if (steps[i].isActive()){

        String[] actionsI = steps[i].getActions();

        for (int k = 0; k < actionsI.length ; k++){
          result[j+k] = actionsI[k];
          j = j + actionsI.length;
        }
      }

    return result;
  }

  protected String[] getActive(){
    String[] result;
    int      size   = 0;
    int      j      = 0;

    for (int i = 0; i < steps.length ; i++)
      if (steps[i].isActive())
        size++;
    result = new String[size];
    for (int i = 0; i < steps.length ; i++)
      if (steps[i].isActive()){
        result[j] = steps[i].getName();
        j++;
      }

    return result;
  }

  public String detailsToString(){
    String result = "";

    for (int i = 0; i < steps.length ; i++)
      result = result + steps[i].toString() + "\n";

    return result;
  }

  public String toString(){
    String result = "";

    for (int i = 0; i < steps.length ; i++){
      result = result + steps[i].getName();
      if (i < steps.length-1)
        result = result + ";";
    }

    return result;
  }
}




