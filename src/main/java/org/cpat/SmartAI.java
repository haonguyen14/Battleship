package org.cpat;

import java.util.*;
import org.cpat.Utilities.*;

class SmartAI extends AI{
 /**
  AI properties
 */
 public boolean   sinkMode;
 public boolean[] sunkShips;  //NEEDS to recieve info on sunk ships from updateMode();
 
 private boolean[] validDir;   //used for sink mode only
 
 private final int up = -10;
 private final int down = 10;
 private final int right = 1;
 private final int left = -1;
 
 private SavedInformation statistic;
 
 private int numXs = 1; //number of TIES for deciding which ship to shoot at
 private char whosNext;
 
 public SmartAI(SavedInformation save){
   super();
   validDir = new boolean[4];
   whosNext = 'X'; 
   
   for(int i = 0; i < 4; i++)
   {
     validDir[i] = true;
   }
   
   sunkShips = new boolean[5];
   for(int i = 0; i < sunkShips.length; i++)
     sunkShips[i] = false;
   
   sinkMode = false;
   statistic = save;
   
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
 
 //NOT sure what this shit does
 public void updateMode()
 {
   if(hitType == 'H')
   {
     if(sinkMode == false)
     {
       sinkMode = true;
     }
   }
   else if(hitType != 'M')
   {
     sinkMode = false;
     for(int i = 0; i < 4; i++)
       validDir[i] = true;
     
     addSunkShip(hitType);
   }
 }
 
 /**
  Return the coordinate of where it should shoot.
  It thinks only!!!!
 */
 public int decideWhereToShoot(){
    int shootHere;
    
    if(sinkMode){
      shootHere = sink();
    }
    else{
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

 /**
  Search Mode
  1) "Places" the whosNext ship into a grid of ALL possible locations for the given game-state, 
     picks the "most likely location"
  2) Breaks any ties by summing ties with the "probability grid" for that ship type (whosNext) 
     loaded from previous game data
  3) All resulting ties are then broken by a X > Y area function, where the tied coordinate with 
     the largest "empty area" surounding it wins.
 */
  private int search(){
    whosNext = skipIfSunk();
    int shootHere;
    
    return shootHere = testShip();
  }
   
  //test ship placement method OR testMap[] "builder", how ever you wanna think about it
  private int testShip(){
    //sum the test "placements" into the testMap array.  
    Integer[][] testMap = addShip(whosNext);  
    testMap = sumWithShipArray(testMap, statistic.getShipArray(whosNext));
    sort(testMap);
    
    for(int x = 0; x < testMap.length;x++)
      System.out.print("[" + testMap[x][0] + ", " + testMap[x][1] + "] ");
    System.out.println("");
    
    numXs = 0;
    Integer[] current = testMap[0];
    int i = 1;
    
    ArrayList<Integer[]> maxArray = new ArrayList<Integer[]>();
    maxArray.add(current);
    
    while(i < testMap.length && current[1] == testMap[i][1])
    {
      maxArray.add(testMap[i]);
      numXs++;
      i++;
    }

    //will tests ties against the saved previous game data "shipArray" *SEE: sumWithShipArray() - ln:290?)
      if(numXs > 0){
        return lookForEmpty(maxArray);
      }
      
    return maxArray.get(numXs)[0];
  }  
  
  
  //addShip (DIFFERENT FROM !!! adding ships in the saveGameData addShip function)
  //this one "adds" ("testMap[coor++]") for every place(square?) the passed ship "type" could overlap 
  //itself for the given gamestate of SmartAI.opponentArray
  
  //Not sure if the logic is right though
  private Integer[][] addShip(char type){
    int coor = 0;
    int x = 1;   //adds to j - horiz
    int y = 10;  //adds to k - vert
    int j = 0;
    int k = 0;
    
    Integer[][] testMap = new Integer[100][2];
    for(int i = 0; i < testMap.length; i++)
    {
      testMap[i][0] = i;
      testMap[i][1] = 0;
    }
    
    Ship ship = new Ship(type, coor, 'H');
    while(coor < 100){
        if(opponentArray[coor] != '0')
        {
          testMap[coor][1] = Integer.MIN_VALUE ;
        }
        else
        {
          if(fakePlaceShip(type, coor, 'H'))
          {
            for(j = coor; j < coor + (ship.length) *x; j+=x)
              testMap[j][1] += 1;
          }
          
          if(fakePlaceShip(type, coor, 'V'))
          {
            for(k = coor; k < coor + (ship.length) *y; k+=y)
              testMap[k][1] += 1;
          }
        }
        
      coor++;
    }
    
    return testMap;
  }
  
  //JUST LIKE plain old place ships EXCEPT it only returns a boolean.  doesn't "add" anything.
  public boolean fakePlaceShip(char type, int coor, char dir){

    Ship newShip = new Ship(type, coor, dir);
    
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
    for(int i = coor; i < coor + (newShip.length) *x; i+=x){
      if(opponentArray[i] != '0'){
        return false;
      }
    }

    return true;
  }
  
  //sum with ShipArray for the given "whosNext" value(s) in testMap  
  private Integer[][] sumWithShipArray(Integer[][] testMap, int[] shipArray){
    for(int i = 0;i < testMap.length; i++)
      testMap[i][1] += shipArray[i];
    return testMap;  
  }    

  
  //look for empty space around each coordinate and whichever has the greatest 
  //number of free-spaces around it is chosen     
  private int lookForEmpty(ArrayList<Integer[]> maxArray){
    
    for(int i = 0; i < maxArray.size(); i++)
      maxArray.get(i)[1] += findArea(maxArray.get(i)[0]);
    
    sort(maxArray);
    
    numXs = 0;
    Integer[] current = maxArray.get(0);
    int i = 1;
    
    while(i < maxArray.size() && current[1] == maxArray.get(i)[1])
    {
      numXs++;
      i++;
    }
    
    for(int x = 0; x < maxArray.size();x++)
      System.out.print("[" + maxArray.get(x)[0] + ", " + maxArray.get(x)[1] + "] ");
    System.out.println("");
    
    //FINAL random decision splitter
    if(numXs > 0){
      Random rand = new Random();
      int coor = rand.nextInt(numXs + 1);
      
      while (opponentArray[maxArray.get(coor)[0]] != '0')
        coor = rand.nextInt(numXs + 1);
      return maxArray.get(coor)[0];
    }
    else
      return maxArray.get(0)[0];

  }
  
  //**looks the same BUT** different form DumbAI quickValidate  (checks a 5x5 area around "coor" and  
  //subtracts the number of (!= '0's) from the total{25}). The "coor" with the highest "val" gets returned.
  //If findArea() returns ties; lookForEmpty() determines "winning coor" randomly 
  private int findArea(int coor){
    int val = 25;
    for(int i = (coor - 22); i <= (coor + 22); i++){
       if(boundry(i) && ((i / 10) >= (coor + 2*up)/10 ) && ((i / 10) <= (coor + 2*down)/10))
       {
         if(((coor + 2*right)%10 >= i%10) && ((coor + 2*left)%10 <= i%10))
         {
           if(opponentArray[i] != '0')
             val--;
         }
       }
    }
    return val;
  }

 private boolean boundry(int outOfBounds){
   if(outOfBounds >= 0 && outOfBounds <= 99){
     return true;
   }
   return false;
 }
//who am I hunting? 
  private char skipIfSunk()
  {
    char type = 'X';
    String map = "CBDSP";
    
    for(int i = 0; i < sunkShips.length; i++)
    {
      if(sunkShips[i] == false)
      {
        type = map.charAt(i);
        break;
      }
    }
    
    return type;
  }
  
//add sunk ship to shipArray
  private void addSunkShip(char type){
    if(type == 'C'){
      sunkShips[0] = true;
    }else if(type =='B'){
      sunkShips[1] = true; 
    }else if(type == 'D'){
      sunkShips[2] = true;
    }else if(type == 'S'){
      sunkShips[3] = true;
    }else
      sunkShips[4] = true;
  }
  
//sort testMap function
  private void sort(Integer[][] a)
  {
    for(int i = 1; i < a.length; i++)
    {
      int j = i;
      while(j > 0 && (a[j][1] > a[j-1][1]))
      {
        Integer[] temp = a[j-1];
        a[j-1] = a[j];
        a[j] = temp;
        
        j--;
      }
    }
  }
  
  private void sort(ArrayList<Integer[]> a)
  {
    for(int i = 1; i < a.size(); i++)
    {
      int j = i;
      while(j > 0 && (a.get(j)[1] > a.get(j-1)[1]))
      {
        Integer[] temp = a.get(j-1);
        a.set(j-1, a.get(j));
        a.set(j, temp);
        
        j--;
      }
    }
  }
  
//CLEANS VARS  
  private int[] clean(int[] washMe){
    for(int i = 0; i < washMe.length; i++){
      washMe[i] = 0;
    }
    return washMe;
  }
  private int clean(int washMe){
      return washMe = 0; 
  }

}
