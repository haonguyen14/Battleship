package org.cpat;

class Battleship
{ 
  public static void main(String[] args)
  {
    GameMenu menu = new GameMenu();
    while(menu.ready == false)
    {
      System.out.println();
    }
    
    //get menu info
    boolean local = menu.local;
    int level = menu.diff;
    String ipAddress = menu.ipAddress;
    
    System.out.println(local + ":" + level);
    
    //initialize the game
    GameView view = new GameView(new GameControllerLocal(), new GameModel(local, level), local);
  }
}
