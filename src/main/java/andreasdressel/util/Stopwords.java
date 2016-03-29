package andreasdressel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 *
 * @author Andreas Dressel
 */
public class Stopwords {
  private final HashSet<String> stopwords;
  private static Stopwords _instance = null;
  private static String stopwordsFile = null;
  
  private Stopwords() {
    this.stopwords = new HashSet<String>();
  }
  
  public static Stopwords getInstance() {
    if(_instance == null) {
      _instance = new Stopwords();
    }
    return _instance;
  }
  
  public HashSet<String> getStopwords() {
    if(this.stopwords.isEmpty()) {
      populateStopwords();
    }
    return this.stopwords;
  }
  
  public static void init(String filename) {
    stopwordsFile = filename;
    //populateStopwords()
  }
  
  private void populateStopwords() {
    if(stopwordsFile == null) {
      System.err.println("Stopwords file must be initialized. Call init(filename)");
    }
    
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(
              new File(stopwordsFile))));
      
      String line = null;
      while((line = reader.readLine()) != null) {
        this.stopwords.add(line);
      }
    } catch(FileNotFoundException ex) {
      ex.printStackTrace();
    } catch(IOException ex) {
      ex.printStackTrace();
    } finally {
      if(reader != null) {
        try {
          reader.close();
        } catch(IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
