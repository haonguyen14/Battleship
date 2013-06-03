package org.cpat;

class GameControllerLocal extends GameController
{
 public GameControllerLocal()
 {
   super();
 }

 public char shoot(Player shooter, Player playerShotAt, int coor)
 {
  /*------------ Debuging ----------------
  for (int i = 0; i < 100; i++)
  {
    if (i % 10 == 0)
      System.out.println("");
    System.out.print(shooter.opponentArray[i] + " ");
  }*/
   
  //update shooter
  char result = playerShotAt.getShotAt(coor);
  
  if(shooter instanceof AI)
  {
   ((AI)shooter).hitType = result;
   if(result == 'H')
     ((AI)shooter).lastHit = coor;

   //update AI mode
   ((AI)shooter).updateMode();

   //update searchArray
   if(shooter instanceof DumbAI)
   {
     for(int i = 0; i < ((DumbAI)shooter).searchArray.length; i++)
     {
       if(coor == ((DumbAI)shooter).searchArray[i])
       {
         ((DumbAI)shooter).searchArrayBool[i] = false;
         break;
       }
     }
   }
   
  }

  if(result == 'M')
  {
   shooter.opponentArray[coor] = 'M';
  }
  else
  {
   shooter.opponentArray[coor] = 'H';
   shooter.incScore();
  }

  return result;
 }
}
