package andreasdressel.hits.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Andreas Dressel
 */
public class HITSGraphParser {
  
  
  
  // file format:
  // *nodes <#nodes>
  // <id1>
  // <id2>
  // ...
  // *edges <#edges>
  // <idx> <idy>
  // ...
  
  public static HashMap<Integer, HITSNode> parseGraph(String filename, HashMap<Integer, HITSNode> graph) {
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
      String line = reader.readLine();
      
      int numberOfNodes = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
      while(numberOfNodes > 0) {
        line = reader.readLine();
        int id = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        
        graph.put(id, new HITSNode(id));
        numberOfNodes--;
      }
      line = reader.readLine();
      
      int numberOfEdges = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
      while(numberOfEdges > 0) {
        line = reader.readLine();
        int id1 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        int id2 = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
        
        // edge from id1 to id2
        HITSNode node1 = graph.get(id1), node2 = graph.get(id2);
        node1.addOutgoingLink(node2);
        node2.addIncomingLink(node1);
        
        numberOfEdges--;
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
    return graph;
  }
}
