/* Generated by Together */

package slime.gui.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import slime.gui.GuiDialog;
import slime.gui.model.*;
import slime.editor.*;
import slime.absynt.*;
import javax.swing.JTextField;
import  javax.swing.*;
import java.awt.*;

public class Ereignishoerer implements ActionListener {

     private GuiDialog gui;
     private JTextField jfield;
     private String t = "dummy";
     private String name = "filedummy";
     private OpenSFC open;
     private SaveSFC save;
     private SFC sfc;
     private Editor edit;
     private JTextField text;



     public Ereignishoerer(JTextField text){
        this.text = text;
     }

     public Ereignishoerer(GuiDialog go){
        this.gui = go;
     }
    //loest eine Aktion nachdem der Anwender den Button gedrueckt hat
    public void actionPerformed(ActionEvent e){
        t = e.getActionCommand();
        if(t.equals("laden")){
        //this.name = gui.getfileName();
            open      = new OpenSFC("vier.sfc");
            this.sfc = open.loadSFC();
            edit = new Editor(sfc);
            JPanel jpanel = new JPanel();
            jpanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            JPanel pospanel = new JPanel();
            pospanel.setLayout(new GridLayout(1,1));
            pospanel.add(edit);
            jpanel.add(pospanel);
            JFrame jFrame = new JFrame();
            jFrame.setContentPane(pospanel);
            jFrame.setSize(1800,1800);
            jFrame.show(true);
            jFrame.setVisible(true);
         }
           if(t.equals("speichern")){
           //u = text.getText();
              String da = "vier.sfc";
              edit      = new Editor();
              //sfc       = edit.getSelectedSFC();
              sfc = Example.getExample1();
              save = new SaveSFC(da,this.sfc);
           }
     }
}