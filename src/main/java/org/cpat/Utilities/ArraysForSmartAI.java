package org.cpat.Utilities;

import java.io.*;

public class ArraysForSmartAI{
  
  
  public static void combineFiles()
  {
    SavedInformation save = getSavedShips(getPath());
    
    String sep = System.getProperty("file.separator");
    String dir = System.getProperty("user.home") + sep + ".Battleship" + sep;
    String file = "data";
    
    int i = 1;
    File fd = new File(dir + file + i + ".bin");
    while(fd.exists())
    {
      SavedInformation s = getSavedShips(dir + file + i + ".bin");
      save.add(s);
      fd.delete();
      
      i++;
      fd = new File(dir + file + i + ".bin");
    }
  }
  
  public static String getPath()
  {
    String sep = System.getProperty("file.separator");
    String dir = System.getProperty("user.home") + sep + ".Battleship" + sep;
    File hDir = new File(dir);
    
    if(!hDir.exists())
      hDir.mkdir();
      
    return dir + "data.bin";
  }
  
  public static void smartSave(SavedInformation saveMe, String fileName)
  {
    try
    {
      ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
      os.writeObject(saveMe);
      os.close();
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
   //getter[][] 
  public static SavedInformation getSavedShips(String fileName)
  {
    SavedInformation saveShips = null;
    try
    {
      if(new File(fileName).exists())
      {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
        saveShips = (SavedInformation)is.readObject();
        is.close();
      }
      else
      {
        saveShips = new SavedInformation();
      }
    } 
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    
    return saveShips;
  }
}
