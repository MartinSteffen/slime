/* Generated by Together */

package slime.gui;

import javax.swing.*;
import slime.gui.listener.*;
import java.awt.*;



public class GuiDialog {
  private String fileName;
  private String da;
  private  JTextField sfcfield ;
  public GuiDialog(){
  }

 /**
   *  built an modal dialog
   */
  public void openSFC(){
        JFrame jframe = new JFrame("Oeffne eine SFCDatei");
        JLabel jlabel = new JLabel("Name der Datei eingeben");
        sfcfield = new JTextField(10);
        JButton jbutton = new JButton("laden");
        //stores the jbutton in a listener list for Ereignishoerer
        jbutton.addActionListener(new Ereignishoerer(this));
        //stores the sfcfield in a listener list for Ereignishoerer
        sfcfield.addActionListener(new Ereignishoerer(sfcfield));
        //panel with Flowlayout
        JPanel openpanel = new JPanel();
        openpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //use another panel to put the component in a Grid
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(2,1));
        jpanel.add(jlabel);
        jpanel.add( sfcfield);
        jpanel.add(jbutton);
        //
        openpanel.add(jpanel);
        jframe.getContentPane().add(openpanel);
        jframe.setSize(300,100);
        jframe.setLocation(0,50);
        jframe.show(true);
        jframe.setVisible(true);
     }
    public String getfileName(){
         /* try{
            da = sfcfield.getText();
          }catch(Exception e){
            System.out.println("jdjdjjd");
          } */
        return sfcfield.getText();
     }

  /**
   * build an modal dialog
   */
  public void saveSFC(){
       //Create labels and text fields
       JPanel openpanel = new JPanel();
       openpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
       JLabel dateiname = new JLabel("SFCName");
       JTextField sfcfield = new JTextField("nichtaendern.sfc",10);
       JButton button = new JButton("speichern");
       //stores the jbutton in a listener list for Ereignishoerer
       button.addActionListener(new Ereignishoerer(this));
       // Layout and display
       JPanel jpanel = new JPanel();
       jpanel.setLayout(new GridLayout(1,1));
       jpanel.add(dateiname);
       jpanel.add(sfcfield);
       JPanel buttonpanel = new JPanel();
       buttonpanel.setLayout(new FlowLayout());
       buttonpanel.add(button);
       //JFrame and display
       openpanel.add(jpanel);
       openpanel.add(buttonpanel);
       JFrame jframe = new JFrame("SFCDatei speichern");
       jframe.setContentPane(openpanel);
       jframe.setLocation(50,50);
       jframe.setSize(200,100);
       jframe.show(true);
       jframe.setVisible(true);
  }
}
