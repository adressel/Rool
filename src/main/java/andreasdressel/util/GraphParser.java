package andreasdressel.util;

import andreasdressel.hits.util.HITSNode;
import andreasdressel.pagerank.util.PageRankNode;
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
  public static HashMap<Integer, PageRankNode> initializePRGraph(String inputFileName) {

    HashMap<Integer, PageRankNode> graph = new HashMap<Integer, PageRankNode>();
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(new File(inputFileName))));
      
      // read the first line containing the number of nodes
      String line = reader.readLine();
      int numberOfNodes = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
      double initialPR = 1.0/numberOfNodes;
            
      // parse all nodes and populate the node dictionary
      while(numberOfNodes > 0) {
        
        line = reader.readLine();
        int id = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        PageRankNode node = new PageRankNode(id, initialPR);
        
        graph.put(id, node);
        numberOfNodes--;
      }
      
      // the next line contains the number of edges
      line = reader.readLine();
      long numberOfEdges = Long.parseLong(line.substring(line.indexOf(" ") + 1));
      
      while(numberOfEdges > 0) {
        // each line contains an edge in the form "id1 id2". This is an edge from the node with
        // id1 to the PageRankNode with id2
        line = reader.readLine();
        
        int from = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        int to = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
        
        PageRankNode f = graph.get(from), t = graph.get(to);

        f.addOutgoingEdge();
        t.addIncomingEdge((PageRankNode)f);
        
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

  public static HashMap<Integer, HITSNode> initializeHITSGraph(String inputFileName) {

    HashMap<Integer, HITSNode> graph = new HashMap<Integer, HITSNode>();
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(new File(inputFileName))));
      
      // read the first line containing the number of nodes
      String line = reader.readLine();
      int numberOfNodes = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
            
      // parse all nodes and populate the node dictionary
      while(numberOfNodes > 0) {
        
        line = reader.readLine();
        int id = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        HITSNode node = new HITSNode(id);
        
        graph.put(id, node);
        numberOfNodes--;
      }
      
      // the next line contains the number of edges
      line = reader.readLine();
      long numberOfEdges = Long.parseLong(line.substring(line.indexOf(" ") + 1));
      
      while(numberOfEdges > 0) {
        // each line contains an edge in the form "id1 id2". This is an edge from the node with
        // id1 to the PageRankNode with id2
        line = reader.readLine();
        
        int from = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        int to = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
        if(from == to) continue; // removes links from node x to itself
        
        HITSNode f = graph.get(from), t = graph.get(to);

        f.addOutgoingLink(t);
        t.addIncomingLink(f);
        
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
