package org.cpat;

abstract class AI extends Player
{
  abstract public int decideWhereToShoot();
  abstract public void updateMode();
  
  public char hitType;
  public int  lastHit;
}