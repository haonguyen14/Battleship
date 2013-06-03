package org.cpat.Utilities;

import java.util.*;

public class DumbAIUtils{

  private static int[] pickingArray1 = {33,66,62,26,84,40,48,11,88,51,15,22,77,44,80,37,59,73,55,4,95,8,91,99,0,19,42,57,75,35,82,17,31,68,24,64,46,53,60,13,86,20,71,79,93,2,39,97,6,28};
    
  private static int[] pickingArray2 = {73,37,22,55,77,59,48,95,4,80,11,62,84,8,51,66,40,33,26,88,15,44,0,91,19,99,82,17,31,68,42,57,75,35,60,86,20,71,24,13,64,46,53,97,6,28,39,79,93,2};
    
  public static int[] getArray()
  {
    Random rand = new Random();
    return (rand.nextInt(2) == 0) ? pickingArray1 : pickingArray2;
  }
  
  //rot 90 degrees (clockwise)
  public static void rotate90(int [] array)
  {
    for(int i = 0; i < array.length; i++)
    {
      int rightDig = array[i] % 10;
      int leftDig = array[i] / 10;
      
      leftDig = (9 - rightDig) * 10;
      rightDig = leftDig;
      
      array[i] = (leftDig + rightDig); 
    }
  }
  
  //rot 180 degrees
  public static void rotate180(int [] array)
  {
    for(int i = 0; i < array.length; i++)
    {
      int rightDig = array[i] % 10;
      int leftDig = array[i] / 10;
       
      rightDig = 9 - rightDig;
      leftDig = (9 - leftDig) * 10;
     
      array[i] = (leftDig + rightDig);    
    }  
  }  
  
  //reverse collumns 
  public static void mirrorVert(int[] array)
  {
    for(int i = 0; i < array.length; i++)
    {
      int rightDig = array[i] % 10;
      int leftDig = array[i] / 10;
   
      leftDig = (9 - leftDig) * 10;
      
      array[i] = (leftDig+rightDig);
    }
  }
  
//reverse rows  
  public static void mirrorHoriz(int[] array)
  {
    for(int i = 0; i < array.length; i++)
    {
      int rightDig = array[i] % 10;
      int leftDig = array[i] / 10;
   
      rightDig = 9 - rightDig;
      
      array[i] = (leftDig+rightDig);
    }
  }
  
//mirror over x=y  
  public static void mirror(int []array)
  {
    for(int i = 0; i < array.length; i++)
    {
      int rightDig = array[i] % 10;
      int leftDig = array[i] / 10;
     
      leftDig = rightDig *10;
      rightDig = leftDig;
      
      array[i] = (leftDig+rightDig);
    }
  }

}