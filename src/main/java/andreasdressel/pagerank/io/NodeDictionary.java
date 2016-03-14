package andreasdressel.pagerank.io;

import andreasdressel.pagerank.util.Node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas Dressel
 */
public class NodeDictionary {
  
  private static NodeDictionary _instance = null;
  private final HashMap<Long, Node> map;
  
  private NodeDictionary() {
    this.map = new HashMap<Long, Node>();
  }
  
  public static synchronized NodeDictionary getInstance() {
    if(_instance == null) {
      _instance = new NodeDictionary();
    }
    return _instance;
  }
  
  public void addNode(long id, Node node) {
    this.map.put(id, node);
  }
  
  public Node getNode(long id) {
    return this.map.get(id);
  }
  
  public void writeRanksToFile(String filename) {
    
    BufferedWriter writer = null;
    
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename))));
      
      for (Map.Entry<Long, Node> entry : this.map.entrySet()) {
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getKey());
        builder.append(" ");
        builder.append(entry.getValue().getRank());
        writer.write(builder.toString());
        writer.newLine();
      }
      
    } catch(IOException ex) {
      ex.printStackTrace();
    } finally {
      if(writer != null) {
        try {
          writer.close();
        } catch(IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
