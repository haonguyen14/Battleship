package org.cpat;

public class Ship {

    char type;
    String name;
    int length;
    boolean sunk;
    int coor;
    char dir;
    
    int hits;

//constructor
  
    public Ship(char type, int coor, char dir){
      this.coor = coor;
      this.dir = dir;
      this.type = Character.toUpperCase(type);
      this.sunk = false;
      this.hits = 0;
      
      if(this.type == 'C'){
        length = 5;
        name = "Carrier";
      }  
      if(this.type == 'B'){
        length = 4;
        name = "Battleship";
      }  
      if(this.type == 'D'){
        length = 3;
        name = "Destroyer";
      }
      if(this.type == 'S'){
        length = 3;
        name = "Submarine";
      }
      if(this.type == 'P'){
        length = 2;
        name = "Patrol Boat";
      }
    }
    
 public void getHit()
 {
   this.hits++;
   if(this.hits == this.length)
     this.sunk = true;
 }
 
 public boolean isSunk() 
 {
  return sunk;
 }
 
 public String getName()
 {
   return name;
 }
 
 public char getType()
 {
   return type;
 }
}
