package slime.simulator;
import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.util.LinkedList;
import  slime.absynt.Declaration;



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: State.java,v 1.2 2002-07-19 09:09:08 swprakt Exp $
 */


class State{
  private Assignment[] environment;

  State(LinkedList decl){
    environment = new Assignment[decl.size()];
    for (int i = 0; i < decl.size() ; i++){
      Declaration temp = (Declaration) decl.get(i);
      environment[i] = new Assignment(temp.var.name, temp.val.val);
    }
  }

  protected void set(String variable, Expression expression){
   for (int i = 0; i < environment.length ; i++)
     environment[i].set(variable, expression.eval(this));
  }

  protected Object get(String variable){
   Object result = new Boolean(false);

   for (int i = 0; i < environment.length ; i++)
     if (environment[i].is(variable))
       result = environment[i].getValue();

    return result;
  }

  protected void read(){
   for (int i = 0; i < environment.length ; i++)
     environment[i].read();
  }

  protected void init(){
    for (int i = 0; i < environment.length ; i++)
      environment[i].init();
  }

  protected JScrollPane createStatePane(Simulator master){
    JScrollPane statePane;
    JPanel      content = new JPanel();

    content.setLayout(new GridLayout(0,1));
    for (int i = 0; i < environment.length ; i++)
      content.add(environment[i].getPanel(master));
    statePane = new JScrollPane(content);
    statePane.setPreferredSize(new Dimension(320, 240));

    return statePane;
  }

  public String toString(){
    String result = "";

    for (int i = 0; i < environment.length ; i++)
      result = result + environment[i].toString() + "\n";

    return result;
  }
}



/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: State.java,v 1.2 2002-07-19 09:09:08 swprakt Exp $
 */


class Assignment{
  private String name;
  private Object initialValue;
  private Object value;

  private JToggleButton editButton;
  private JSlider       editSlider;

  Assignment(String nam, Object initVal){
    name            = nam;
    initialValue    = initVal;
    value           = initVal;

  }

  protected boolean is(String variable){
    return name.equals(variable);
  }

  protected void set(String variable, Object val){
    if (name.equals(variable)){
      value = val;
      if (value instanceof Boolean) {
        editButton.setSelected(((Boolean) value).booleanValue());
        editButton.setText(value.toString());
      } else {
        editSlider.setValue(((Integer) value).intValue());
      }
    }
  }

  protected Object getValue(){
    return value;
  }

  protected void read(){
    if (value instanceof Boolean){
      value = new Boolean(editButton.isSelected());
      editButton.setText(value.toString());
    } else {
      value = new Integer(editSlider.getValue());
    }
  }

  protected void init(){
    value = initialValue;
    if (value instanceof Boolean) {
      editButton.setSelected(((Boolean) value).booleanValue());
      editButton.setText(value.toString());
    } else {
      editSlider.setValue(((Integer) value).intValue());
    }
  }

  protected JPanel getPanel(Simulator master){
    JPanel  assignmentPanel = new JPanel();

    assignmentPanel.setLayout(new GridLayout(0,2));
    if (value instanceof Boolean) {
      assignmentPanel.add(new JLabel(name, JLabel.CENTER));
      editButton = new JToggleButton(value.toString(), ((Boolean) value).booleanValue());
      editButton.setActionCommand("change");
      editButton.addActionListener(master);
      assignmentPanel.add(editButton);
    } else {
      assignmentPanel.add(new JLabel(name, JLabel.CENTER));
      editSlider = new JSlider(0, 10,((Integer) value).intValue());
      editSlider.addChangeListener(master);
      editSlider.setPaintTicks(true);
      editSlider.setMajorTickSpacing(10);
      editSlider.setMinorTickSpacing(2);
      assignmentPanel.add(editSlider);
    }

    return assignmentPanel;
  }

  public void actionPerformed(ActionEvent e){
    if (e.getActionCommand() == "change")
      value = new Boolean(editButton.isSelected());
  }

  public String toString(){
    String result;

    result = "[" + name + "->" + value.toString() + "]";

    return result;
  }
}




