package org.cpat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

import java.util.*;
import org.cpat.Utilities.*;

public class GameView
{
  //GUI specific components
  private Frame f;
  private Button[] bottomPanelButtons;
  private Button[] topPanelButtons;
  
  
  private char shipType;
  private char direction;
  private boolean local;
  
  private GameController mController;
  private GameModel mModel;

  public GameView(GameController controller, GameModel model, boolean pLocal)
  {
    f = new UIFrame("Battleship");
    f.setSize(600, 625);
    f.setLocation(600,50);
    f.setVisible(true);
    
    shipType = 'C';
    direction = 'H';
    local = pLocal;
    
    mController = controller;
    mModel = model;
  }
  
  class UIFrame extends Frame
  {
    public UIFrame(String title) {
      super(title);
      setBackground(Color.white);
      
      //---------------------Initialize Button Arrays---------------------------
      topPanelButtons = new Button[100];
      bottomPanelButtons = new Button[100];
      //------------------------------------------------------------------------
      
      //---------------------Button Listeners ----------------------------------
      ShootButton shootListener = new ShootButton();
      ShipTypeButton shipTypeListener = new ShipTypeButton();
      PlaceShipButton placeShipListener = new PlaceShipButton();
      
      OrientationCheckbox orientaionListener = new OrientationCheckbox();
      //------------------------------------------------------------------------
      
      
      //---------------------Center Button Panel for Ships----------------------
      
      Panel buttonPanel3 = new Panel();
      buttonPanel3.setLayout(new GridLayout(1, 5));
      
      Button btCarrier = new Button("Carrier");
      btCarrier.addActionListener(shipTypeListener);
      btCarrier.setActionCommand("C");
      
      Button btBattleship = new Button("Battleship");
      btBattleship.addActionListener(shipTypeListener);
      btBattleship.setActionCommand("B");
      
      Button btSubmarine = new Button("Submarine");
      btSubmarine.addActionListener(shipTypeListener);
      btSubmarine.setActionCommand("S");
      
      Button btDestroyer = new Button("Destroyer");
      btDestroyer.addActionListener(shipTypeListener);
      btDestroyer.setActionCommand("D");
      
      Button btPBoat = new Button("Patrol Boat");
      btPBoat.addActionListener(shipTypeListener);
      btPBoat.setActionCommand("P");
      
      buttonPanel3.add(btCarrier);
      buttonPanel3.add(btBattleship);
      buttonPanel3.add(btSubmarine);
      buttonPanel3.add(btDestroyer);
      buttonPanel3.add(btPBoat);
      //-----------------------------------------------------
      
      
      Panel centerPanel = new Panel();
      centerPanel.add(buttonPanel3);
      add("North", centerPanel);
      
      //-------------Center Button panel for orientation---------------------
      
      Panel checkboxPanel = new Panel();
      checkboxPanel.setLayout(new GridLayout(1, 2));
      
      CheckboxGroup orientation = new CheckboxGroup();
      Checkbox horizontalBox = new Checkbox("Horizontal", orientation, true);
      horizontalBox.addItemListener(orientaionListener);
      
      Checkbox verticalBox = new Checkbox("Vertical", orientation, false);
      verticalBox.addItemListener(orientaionListener);
      
      checkboxPanel.add(horizontalBox);
      checkboxPanel.add(verticalBox);
      
      Panel centerPanel2 = new Panel();
      centerPanel2.add(checkboxPanel);
      add("South", centerPanel2);
      
      Panel centerPanel3 = new Panel();
      centerPanel3.setLayout(new GridLayout(2, 1));
      centerPanel3.add(centerPanel);
      centerPanel3.add(centerPanel2);
      add("Center", centerPanel3);
      
      int numID = 0;
      //---------------------------------Top shot panel----------------------------------------------
      Panel buttonPanel1 = new Panel();
      buttonPanel1.setLayout(new GridLayout(11, 11));
      for (int player1 = 0; player1 <= 120; player1++){
        if (player1 == 0){
          buttonPanel1.add(new Button(""));
        }
        else if (player1 <= 10)
          buttonPanel1.add(new Button(player1 + ""));
        else
        {
          if((player1 % 11) == 0)
          {
            buttonPanel1.add(new Button(Character.toString((char)(65 + (((player1-11) / 11))))));
          }
          else
          {
            topPanelButtons[numID] = new Button("");
            topPanelButtons[numID].setActionCommand(numID + "");
            topPanelButtons[numID].addActionListener(shootListener);
            buttonPanel1.add(topPanelButtons[numID]);
            numID++;
          }
        }
      }
      
      //Panel added, North aligned
      Panel topPanel = new Panel();
      //Panel centerPanel1 = new Panel();
      topPanel.add(buttonPanel1);
      
      //centerPanel1.add(buttonPanel1);
      add("North", topPanel);
      
      //add("West", centerPanel1);    
      
      //Bottom shot panel
      numID = 0;
      Panel buttonPanel2 = new Panel();
      buttonPanel2.setLayout(new GridLayout(11, 11));
      for (int player2 = 0; player2 <= 120; player2++){
        if (player2 == 0)
          buttonPanel2.add(new Button(""));
        else if (player2 <= 10)
          buttonPanel2.add(new Button(player2 + ""));
        else
        {
          if((player2 % 11) == 0)
          {
            buttonPanel2.add(new Button(Character.toString((char)(65 + (((player2-11) / 11))))));
          }
          else
          {
            bottomPanelButtons[numID] = new Button("");
            bottomPanelButtons[numID].setActionCommand(numID + "");
            bottomPanelButtons[numID].addActionListener(placeShipListener);
            buttonPanel2.add(bottomPanelButtons[numID]);
            numID++;
          }
        }
      }
      
      
      //Panel added, Center aligned
      Panel bottomPanel = new Panel();
      bottomPanel.add(buttonPanel2);
      add("South", bottomPanel);
      
      addWindowListener(new WindowCloser());
    }
    
    class ShootButton implements ActionListener 
    {
      public void actionPerformed(ActionEvent evt) 
      {
        topPanelButtons[Integer.parseInt(evt.getActionCommand())].setEnabled(false);
        if (mController.shoot(mModel.mPlayer1, mModel.mPlayer2, Integer.parseInt(evt.getActionCommand())) != 'M')//can be both 'S' and 'H'
          topPanelButtons[Integer.parseInt(evt.getActionCommand())].setLabel("X");
        else
          topPanelButtons[Integer.parseInt(evt.getActionCommand())].setLabel("O");

        if(mModel.mPlayer1.getScore() < 17)
        {
          if(local)
          {
            int AIShot = ((AI)mModel.mPlayer2).decideWhereToShoot();
            if (mController.shoot(mModel.mPlayer2, mModel.mPlayer1, AIShot) != 'M')
              bottomPanelButtons[AIShot].setLabel("X");
            else
              bottomPanelButtons[AIShot].setLabel("O");
            
            if(mModel.mPlayer2.getScore() >= 17)
            {
              //player2 wins
              JOptionPane.showMessageDialog(null, "Player 2 wins!", "Player 2 wins!", JOptionPane.INFORMATION_MESSAGE);
              f.setEnabled(false);
            }
          }
          else
          {
            //wait for shot from the other player
          }
        }
        else
        {
          //player1 wins
          JOptionPane.showMessageDialog(null, "Player 1 wins!", "Player 1 wins!", JOptionPane.INFORMATION_MESSAGE);
          f.setEnabled(false);
        }
      }
    }
    
    class ShipTypeButton implements ActionListener 
    {
      public void actionPerformed(ActionEvent evt) 
      {
        shipType = evt.getActionCommand().charAt(0);
        System.out.println("Shiptype: " + shipType);
      }
    }
    
    class OrientationCheckbox implements ItemListener
    {
      public void itemStateChanged(ItemEvent e)
      {
        direction = e.getItem().toString().charAt(0);
        System.out.println("Direction: " + direction);
      }
    }
    
    class PlaceShipButton implements ActionListener
    {
      public void actionPerformed(ActionEvent evt)
      {
        if(mController.placeShip(mModel.mPlayer1, shipType, Integer.parseInt(evt.getActionCommand()), direction))
        {
          Ship newShip = new Ship(shipType, Integer.parseInt(evt.getActionCommand()), direction);
          int step = (direction == 'H') ? 1 : 10;
          
          for(int i = Integer.parseInt(evt.getActionCommand()); i < Integer.parseInt(evt.getActionCommand()) + (newShip.length * step); i += step)
          {
            bottomPanelButtons[i].setEnabled(false);
            bottomPanelButtons[i].setLabel(shipType + "");
          }
        }
      }
    }
    
    class WindowCloser extends WindowAdapter {
      public void windowClosing(WindowEvent evt) {
        String fileName = ArraysForSmartAI.getPath();
        mController.saveGame(mModel.mPlayer1, fileName);
        ArraysForSmartAI.combineFiles();
        System.exit(0);
      }
    }
  }
}

