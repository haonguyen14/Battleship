package org.cpat;

import java.util.*;
import org.cpat.Utilities.DumbAIUtils;


class DumbAI extends AI{
 /**
  AI properties
 */
 public int[] searchArray;

 public boolean[] searchArrayBool;
 public boolean   sinkMode;
 
 private boolean[] validDir;   //used for sink mode only
 
 private final int up = -10;
 private final int down = 10;
 private final int right = 1;
 private final int left = -1;
 
 public DumbAI(int[] tempSearchArray){
   super();
  
   searchArray = tempSearchArray;
   searchArrayBool = new boolean[searchArray.length];
   validDir = new boolean[4];
   
   for(int i = 0; i < searchArray.length; i++){
     searchArrayBool[i] = true;
   }

   for(int i = 0; i < 4; i++){
     validDir[i] = true;
   }
   
   sinkMode = false;
   
   
    /**
    * Randomly fill the board
    * Not very fast though - implement GUI for player1 later!!!
    */
    Random rand = new Random();
    while (!placeShip('C', rand.nextInt(100), ((rand.nextInt(2) == 0) ? 'V' : 'H'))) {}
    while (!placeShip('B', rand.nextInt(100), ((rand.nextInt(2) == 0) ? 'V' : 'H'))) {}
    while (!placeShip('D', rand.nextInt(100), ((rand.nextInt(2) == 0) ? 'V' : 'H'))) {}
    while (!placeShip('S', rand.nextInt(100), ((rand.nextInt(2) == 0) ? 'V' : 'H'))) {}
    while (!placeShip('P', rand.nextInt(100), ((rand.nextInt(2) == 0) ? 'V' : 'H'))) {}
 }

 
 public void updateMode()
 {
   if(hitType == 'H')
   {
     if(sinkMode == false)
       sinkMode = true;
   }
   else if(hitType != 'M')
   {
     sinkMode = false;
     for(int i = 0; i < 4; i++)
       validDir[i] = true;
   }
 }
 
 /**
  Return the coordinate of where it should shoot.
  It thinks only!!!!
 */
 public int decideWhereToShoot(){
    int shootHere;

    if(sinkMode)
    {
      shootHere = sink();
    }
    else
    {
      shootHere = search();
    }
  return shootHere; 
 }

 /**
  Search Mode
 */
  private int search(){
    int shootHere = -1;
    for(int i = 0; i < searchArrayBool.length; i++){
      if(searchArrayBool[i] == true){
        shootHere = searchArray[i];
      }
    }
    
    if(shootHere == -1)
    {
      for(int i = 0; i < searchArrayBool.length; i++)
 searchArrayBool[i] = true;
      DumbAIUtils.mirror(searchArray);
      shootHere = search();
    }
    return shootHere;
  }

 /**
  Sink Mode
  It goes on either vertical or horizontal direction during the sink mode.
  if dead end -> Leave sink mode when all directions are invalid. 
    Otherwise, sink mode is turned of by sinking a ship 
 */
 private int sink()
 {  
   //we are using fall through method of thinking. Be careful of else if!!!
   System.out.println(lastHit);
   int guess = 0;
   
   if(validDir[0] == true)//look up
   {
     guess = lastHit + up;
     while(guess >= 0 && opponentArray[guess] == 'H')
       guess = guess + up;
     
     if(guess < 0 || opponentArray[guess] == 'M')
       validDir[0] = false;
     else
       return guess;
   }
   
   if(validDir[2] == true)//look down
   {
     guess = lastHit + down;
     while(guess <= 99 && opponentArray[guess] == 'H')
       guess = guess + down;
     
     if(guess > 99 || opponentArray[guess] == 'M' )
       validDir[2] = false;
     else
       return guess;
   }
   
   if(validDir[1])//look right
   {  
     guess = lastHit + right;
     while(((guess % 10) > (lastHit % 10)) && opponentArray[guess] == 'H')
       guess = guess + right;
     
     if(((guess % 10) <= (lastHit % 10)) || opponentArray[guess] == 'M')
       validDir[1] = false;
     else
       return guess;
   }
   
   if(validDir[3])//look left
   {
     guess = lastHit + left;
     while(((guess % 10) < (lastHit % 10)) && opponentArray[guess] == 'H')
       guess = guess + left;
     
     if(((guess % 10) >= (lastHit % 10)) || opponentArray[guess] == 'M')
       validDir[3] = false;
     else
       return guess;
   }
   
   return -1;
 }
   
}
