package slime.simulator;



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: <dollar>
 */


class Step{
  private String   name;
  private String[] actions;
  private boolean  active;
  private boolean  ready;

  Step(slime.absynt.Step stp){
    name    = stp.name;
    actions = new String[stp.actions.size()];
    for (int i = 0; i < stp.actions.size() ; i++)
      actions[i] = ((slime.absynt.StepAction) stp.actions.get(i)).a_name;
  }

  protected String getName(){
    return name;
  }

  protected String[] getActions(){
    return actions;
  }

  protected boolean isActive(){
    return active;
  }

  protected boolean containAction(String name){
    boolean result = false;

    for (int i = 0; i < actions.length ; i++)
      if (actions[i].equals(name))
        result = true;

    return result;
  }

  protected void setReady(String step){
    if (name.equals(step))
      ready = true;
  }

  protected void deactivate(String step){
    if (name.equals(step))
      active = false;
  }

  protected void activate(){
    if (!active)
      active = ready;
    ready = false;
  }

  protected void init(String initialStep){
    ready  = false;
    if (name.equals(initialStep))
      active = true;
    else
      active = false;
  }

  public String toString(){
    String result;

    result = name + isActive() + "::=";
    for (int i = 0; i < actions.length ; i++){
      result = result + actions[i];
      if (i < actions.length-1)
        result = result + ";";
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

