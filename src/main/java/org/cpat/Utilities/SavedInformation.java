package org.cpat.Utilities;

import java.io.Serializable;

public class SavedInformation implements Serializable
{
  public int[][] shipData;
  
  public SavedInformation()
  {
    shipData = new int[5][100];
    
    for(int i = 0; i < shipData.length; i++)
    {
       for(int j = 0; j < shipData[i].length; j++)
         shipData[i][j] = 0;
     }
  }
  
  public SavedInformation(SavedInformation save)
  {
   shipData = save.shipData;
  }
  
  public void add(SavedInformation save)
  {
    for(int i = 0; i < shipData.length; i++)
    {
      for(int j = 0; j < shipData[i].length; j++)
      {
        shipData[i][j] += save.shipData[i][j];
      }
    }
  }
  
  public int[] getShipArray(char type)
  {
    int[] ret = null;
    System.out.println("Trying to get " + Character.toUpperCase(type));
    switch(Character.toUpperCase(type))
    {
      case 'C':
        ret =  shipData[0];
        break;
      case 'B':
        ret = shipData[1];
        break;
      case 'D':
        ret = shipData[2];
        break;
      case 'S':
        ret = shipData[3];
        break;
      case 'P':
        ret = shipData[4];
        break;
    }
    
    return ret;
  }
}