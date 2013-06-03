package org.cpat;

import java.util.*;

public class Player {
  public char[] myArray;
  public char[] opponentArray; 
  
  private Hashtable<Character, Ship> ships;
  private int score;
  
//constructor
  public Player(){
    myArray = new char[100];
    opponentArray = new char[100];
    fillBoard();
    
    ships = new Hashtable<Character, Ship>();
  }
 
  
  public char getShotAt(int coor)
  {
    System.out.println("Get Shot At: " + coor);
    char ret = '0';

    if(Character.toUpperCase(myArray[coor]) == '0')
    {
      ret = 'M';
      myArray[coor] = 'M';
    }
    else
    {
      Ship ship = ships.get(Character.toUpperCase(myArray[coor]));
      ship.getHit();
      
      if(ship.isSunk())
        ret = ship.type;
      else
        ret = 'H';
      
      myArray[coor] = 'H';
    }
    
    return ret;
  }
  
  //plain old place ships 
  public boolean placeShip(char type, int coor, char dir)
  { 
    Ship newShip = new Ship(type, coor, dir);
    
    //checking for validity
    if(ships.containsKey(type))
      return false;
    
    int x = 0;
    if(coor < 0 || coor > 99){
      return false;
    }
    if(dir == 'H'){
      x = 1;
      if(((coor + newShip.length - 1) % 10) <= (coor % 10)){
        return false;
      }  
    }
    else if(dir == 'V'){
      x = 10;
      if((coor + (newShip.length - 1) * 10) > 99){
        return false;
      }
    }    
    
    for(int i = coor; i < coor + (newShip.length) * x; i += x)
    {
      if(myArray[i] != '0'){
        return false;
      }
    }

    //add ship to table of ships
    ships.put(newShip.getType(), newShip);
    
    for(int i = coor; i < coor + (newShip.length) * x; i += x)
    {
      myArray[i] = type;
    }
    return true;
  }
  
  private void fillBoard(){
    for(int i = 0; i < myArray.length; i++){
      myArray[i] = '0';
      opponentArray[i] = '0';
    }
  }
  
  public int getScore()
  {
    return score;
  }

  public void incScore()
  {
    score++;
  } 
  
  public Ship getShip(char type)
  {
    return ships.get(type);
  }
}
