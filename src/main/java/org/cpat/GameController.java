package org.cpat;

import org.cpat.Utilities.*;
import java.io.File;

abstract class GameController
{
  public GameController()
  {
  }
  
  abstract public char shoot(Player shooter, Player playerShotAt, int coor);
  
  public boolean placeShip(Player player, char type, int coor, char dir)
  {
    return player.placeShip(type, coor, dir);
  }
  
  public void saveGame(Player player, String fileName)
  {
    SavedInformation shipData = null;
    
    if(new File(fileName).exists())
    {
      shipData = ArraysForSmartAI.getSavedShips(fileName);
    }
    else
    {
      shipData = new SavedInformation();
    }
    
    //------------------prepare data------------------------
    String symbols = "CBDSP";
    for(int i = 0; i < 5; i++)
    {
      Ship ship = player.getShip(symbols.charAt(i));
      int coor = ship.coor;
      int dir = (ship.dir == 'H') ? 1 : 10;
      
      for(int j = coor; j < ((ship.length*dir) + coor); j = j + dir)
      {
        shipData.shipData[i][j]++;
      }
    }
    //---------------------------------------------------------
    System.out.println("Saved");
    ArraysForSmartAI.smartSave(shipData, fileName);
  }
}