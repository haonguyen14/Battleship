package org.cpat;

import org.cpat.Utilities.*;

class GameModel
{
  Player mPlayer1;
  Player mPlayer2;
  
  public GameModel(boolean local, int level)
  {
    
    mPlayer1 = new Player();
    
    if(local)
    {
      if(level == 1)
      {
        mPlayer2 = new DumbAI(DumbAIUtils.getArray());
      }
      else
      {
        SavedInformation save = ArraysForSmartAI.getSavedShips(ArraysForSmartAI.getPath());
        ArraysForSmartAI.getPath();
        mPlayer2 = new SmartAI(save);
      }
    }
    else
    {
      mPlayer2 = new Player();
    }
  }
}
