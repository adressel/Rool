package andreasdressel.pagerank.io;

import andreasdressel.pagerank.util.PageRankNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Dressel
 */
public class GraphParser {
  
  /**
   * FILE FORMAT:
    
    #nodes numberOfNodes
    1 nodeTitle
    2 nodeTitle
    ...
    #edges numberOfEdges
    1 2
    2 3
    3 1
    ...
   * 
   * @param inputFileName
   * @return 
   */
  public static List<PageRankNode> initializeGraph(String inputFileName) {

    List<PageRankNode> graph = new ArrayList<PageRankNode>(); 
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(new File(inputFileName))));
      
      // read the first line containing the number of nodes
      String line = reader.readLine();
      long numberOfNodes = Long.parseLong(line.substring(line.indexOf(" ") + 1));
      double initialPR = 1.0/numberOfNodes;
      
      // does LinkedList perform better since we only add and iterate?
      
      // parse all nodes and populate the node dictionary
      NodeDictionary dictionary = NodeDictionary.getInstance();
      while(numberOfNodes > 0) {
        
        line = reader.readLine();
        int id = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        
        PageRankNode node = new PageRankNode(id, initialPR, 0);
        
        dictionary.addNode(id, node);
        graph.add(node);
        numberOfNodes--;
      }
      
      
      // the next line contains the number of edges
      line = reader.readLine();
      long numberOfEdges = Long.parseLong(line.substring(line.indexOf(" ") + 1));
      
      while(numberOfEdges > 0) {
        // each line contains an edge in the form "id1 id2". This is an edge from the node with
        // id1 to the PageRankNode with id2
        line = reader.readLine();
        
        long from = Long.parseLong(line.substring(0, line.indexOf(" ")));
        long to = Long.parseLong(line.substring(line.indexOf(" ") + 1));
        
        PageRankNode f = dictionary.getNode(from);
        f.addOutgoingEdge();
        dictionary.getNode(to).addIncomingEdge(f);
        
        numberOfEdges--;
      }
      
      
    } catch(FileNotFoundException ex) {
      System.err.println("Input file not found.");
      ex.printStackTrace();
    } catch(IOException ex) {
      // could happen if number of nodes or number of edges incorrect
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
