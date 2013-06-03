package org.cpat;

import java.awt.*;
import java.awt.event.*;

public class GameMenu
{
 public  boolean local;
 public  int diff;
 public  String ipAddress;
 public  boolean ready;
 
 private  Panel subPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
 private  Panel subPanel2 = new Panel(new GridLayout(2, 1));
 
 private GameMenuFrame frame;
  
 public GameMenu()
 {
  local = true;
  diff = 1;
  ipAddress = "";
  ready = false;
   
  frame = new GameMenuFrame("Battleship");
  frame.setSize(300, 260);
  frame.setLocation(600, 50);
  frame.setVisible(true);
  frame.setResizable(false);
 }

 public  class GameMenuFrame extends Frame
 {
  public GameMenuFrame(String title)
  {
   super(title);
   setBackground(Color.white);
   
   //--------------------Set up listeners--------------------
   DiffListener diffListener = new DiffListener();
   //--------------------------------------------------------
 
   
   //--------------------Set up GameMode buttons--------------
   Panel mainPanel = new Panel(new GridLayout(2, 2, 10, 10));
   
   CheckboxGroup cbgGameMode = new CheckboxGroup();
   Checkbox cbAI = new Checkbox("Player vs AI", cbgGameMode, true);
   cbAI.addItemListener(new GameModeListener());
   Checkbox cbNet = new Checkbox("Player vs Player", cbgGameMode, false);
   cbNet.addItemListener(new GameModeListener());
   mainPanel.add(cbAI);
   mainPanel.add(cbNet);


   CheckboxGroup cbgDiff = new CheckboxGroup();
   Checkbox cbEasy = new Checkbox("Easy", cbgDiff, true);
   cbEasy.addItemListener(diffListener);
   Checkbox cbHard = new Checkbox("Hard", cbgDiff, false);
   cbHard.addItemListener(diffListener);
   subPanel.add(cbEasy);
   subPanel.add(cbHard);
   mainPanel.add(subPanel);


   subPanel2.add(new Label("IP address"));
   TextField txtIp = new TextField();
   subPanel2.add(txtIp);
   subPanel2.setSize(50, 50);
   subPanel2.setEnabled(false);
   mainPanel.add(subPanel2);
   //--------------------------------------------------------

   Button btPlay = new Button("Play");
   btPlay.addActionListener(new PlayListener());
   btPlay.setSize(10, 20);

   setLayout(new FlowLayout(FlowLayout.CENTER));
   
   Panel childPanel = new Panel(new GridLayout(2, 1, 0, 10));
   childPanel.add(mainPanel);
   childPanel.add(btPlay);
   
   add(childPanel);

   addWindowListener(new WindowCloser());  
  }
 }

 public class WindowCloser extends WindowAdapter
 {
	public void windowClosing(WindowEvent evt)
	{
		System.exit(0);
	}
 }
 
 public  class GameModeListener implements ItemListener
 { 
   Panel disableThis;
   Panel enableThis;
   
   public void itemStateChanged(ItemEvent e)
   {
     if(local)
     {
       local = false;
       subPanel.setEnabled(false);
       subPanel2.setEnabled(true);
     }
     else
     {
       local = true;
       subPanel.setEnabled(true);
       subPanel2.setEnabled(false);
     }
   }
 }
 
 public  class DiffListener implements ItemListener
 { 
   public void itemStateChanged(ItemEvent e)
   {
     if(diff == 1)
       diff = 2;
     else
       diff = 1;
   }
 }
 
 public class PlayListener implements ActionListener
 {
   public void actionPerformed(ActionEvent evt)
   {
     ready = true;
     frame.setVisible(false);
     frame.dispose();
   }
 }
}
