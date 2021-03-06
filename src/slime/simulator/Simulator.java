package slime.simulator;
import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;
import  javax.swing.event.*;
import  slime.absynt.SFC;
import  slime.editor.Editor;

/**
 * some description
 * Bla
 * @author Initially provided by Immo Grabe.
 * @version $Id: Simulator.java,v 1.5 2002-07-19 09:09:07 swprakt Exp $
 */


public class Simulator extends JFrame implements ActionListener, ChangeListener, WindowListener{
  private SimSFC      sfc;
  private Editor      editor;

  private JSplitPane  contentPane;

  private JScrollPane informationPane;
  private JSplitPane  executionPane;
  private JPanel      actionPane;

  private JTextArea   sfcDisplay;

  private JButton     startButton;
  private JButton     stepButton;

  private JList       possibleActions;

  Simulator(SFC model){
    super("SFC simulator v0.2");
    sfc = new SimSFC(model);

    createInformationPane();
    createExecutionPane();
    contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, informationPane, executionPane);

    addWindowListener(this);
    setContentPane(contentPane);

    init();

    pack();
    setVisible(true);
  }

  public Simulator(Editor ed){
    super("SFC simulator v0.2");
    editor = ed;
    sfc    = new SimSFC(editor.getSelectedSFC());

    createInformationPane();
    createExecutionPane();
    contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, informationPane, executionPane);

    addWindowListener(this);
    setContentPane(contentPane);

    init();

    pack();
    setVisible(true);
  }

  private void createInformationPane(){
    sfcDisplay = new JTextArea(sfc.toString());
    sfcDisplay.setEditable(false);
    informationPane = new JScrollPane();
    informationPane.setViewportView(sfcDisplay);
    informationPane.setPreferredSize(new Dimension(320, 480));
  }

  private void createExecutionPane(){
    createActionPane();
    executionPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
    executionPane.setTopComponent(sfc.createStatePane(this));
    executionPane.setBottomComponent(actionPane);
  }

  private void createActionPane(){
    JScrollPane actionChooser;

    actionPane = new JPanel();

    GridBagLayout      gridbag = new GridBagLayout();
    GridBagConstraints c       = new GridBagConstraints();
    actionPane.setLayout(gridbag);
    c.fill = GridBagConstraints.HORIZONTAL;

    startButton = new JButton("restart");
    startButton.setActionCommand("Start");
    startButton.addActionListener(this);
    c.weightx = 1.0;
    c.weighty = 0.0;
    c.gridx   = 0;
    c.gridy   = 0;
    gridbag.setConstraints(startButton, c);
    actionPane.add(startButton);

    possibleActions = new JList();
    possibleActions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    actionChooser = new JScrollPane(possibleActions);
    c.weighty = 1.0;
    c.gridx   = 0;
    c.gridy   = 1;
    gridbag.setConstraints(actionChooser, c);
    actionPane.add(actionChooser);

    stepButton = new JButton("choose action");
    stepButton.setActionCommand("Step");
    stepButton.addActionListener(this);
    c.weighty = 0.0;
    c.gridx   = 0;
    c.gridy   = 2;
    gridbag.setConstraints(stepButton, c);
    actionPane.add(stepButton);

    actionPane.setPreferredSize(new Dimension(320, 240));
  }

  private void init(){
    start();
    possibleActions.setListData(sfc.getPossibleActions());
    sfcDisplay.setText(sfc.toString());
  }

  private void start(){
    sfc.start();
    stepButton.setText("choose action");
  }

  public void actionPerformed(ActionEvent e){
    if (e.getActionCommand() == "Start")
      start();
    if (e.getActionCommand() == "Step"){
      sfc.perform((String) possibleActions.getSelectedValue());
      if (sfc.getPossibleActions().length == 0){
        if (stepButton.getText().equals("choose action"))
          stepButton.setText("choose transition");
        else
          stepButton.setText("choose action");
        sfc.changePhase();
      }
    }
    if (e.getActionCommand() == "change")
      sfc.read();
    possibleActions.setListData(sfc.getPossibleActions());
    sfcDisplay.setText(sfc.toString());
  }

  public void stateChanged(ChangeEvent e){
    sfc.read();
    possibleActions.setListData(sfc.getPossibleActions());
    sfcDisplay.setText(sfc.toString());
  }

  public void windowClosing(WindowEvent e){
      this.dispose(); //System.exit(0);
  }

  public void windowActivated(WindowEvent e){
  }

  public void windowDeactivated(WindowEvent e){
  }

  public void windowIconified(WindowEvent e){
  }

  public void windowDeiconified(WindowEvent e){
  }

  public void windowClosed(WindowEvent e){
  }

  public void windowOpened(WindowEvent e){
  }
}



