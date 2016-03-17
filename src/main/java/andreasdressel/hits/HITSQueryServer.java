/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andreasdressel.hits;

import andreasdressel.hits.util.Node;
import andreasdressel.server.Server;
import andreasdressel.server.util.QueryHit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andreas Dressel
 */
public final class HITSQueryServer extends Server {
  
  private final HashMap<String, HashSet<Node>> invertedIndex;
  
  
  private HITSQueryServer(int port, String filename) {
    super(port, filename);
    
    this.invertedIndex = new HashMap<String, HashSet<Node>>();
    initServer();
    
  }
  
  @Override
  public void initServer() {
    // read in the inverted index file and start the server
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<QueryHit> processQuery(String query) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private synchronized Set<Node> computeHITS(String query, int iterations) {
    if(query == null) {
      System.err.println("Query must not be null. (computeHITS)");
      return null;
    }
    
    // this implementation is not case-sensitive
    query = query.toLowerCase();
    
    //@todo: handle stop words
    
    
    // 1. compute base set
    HashSet<Node> seedSet = new HashSet<Node>();
    getBaseSet(query, seedSet);
    
    // 2. extend base set to seed set
    extendToSeedSet(seedSet);
    
    // 3. iteratively calculate hits values, and normalize after each step
    double sumOfSquaredAuthScores = 0, sumOfSquaredHubScores = 0;
    while(iterations > 0) {
      
      for(Node node : seedSet) {
        sumOfSquaredAuthScores += Math.pow(node.calculateNewAuthScore(), 2);
        sumOfSquaredHubScores += Math.pow(node.calculateNewHubScore(), 2);
      }
      
      sumOfSquaredAuthScores = Math.sqrt(sumOfSquaredAuthScores);
      sumOfSquaredHubScores = Math.sqrt(sumOfSquaredHubScores);
      
      for(Node node : seedSet) {
        node.normalizeScores(sumOfSquaredAuthScores, sumOfSquaredHubScores);
      }
      
      iterations--;
      if(iterations > 0) {
        updateOldScores(seedSet);
      }
    }
    
    return seedSet;
  }
  
  
  private void getBaseSet(String query, HashSet<Node> set) {
    // base set has to contain all words from the query (AND)
    // this is one possibile implementation choice (another one would be to allow phrases)
    
    String[] queryElements = query.split(" ");
    int i = 0;
    while(set.isEmpty() && i < queryElements.length) {
      if(this.invertedIndex.containsKey(queryElements[i])) {
        set.addAll(this.invertedIndex.get(queryElements[i]));
      }
      i++;
    }
    
    while(i < queryElements.length) {
      if(this.invertedIndex.containsKey(queryElements[i])) {
        set.retainAll(this.invertedIndex.get(queryElements[i]));
      }
      i++;
    }
  }
  
  private void extendToSeedSet(HashSet<Node> set) {
    for(Node node : set) {
      set.addAll(node.getOutgoingLinks());
      set.addAll(node.getIncomingLinks());
    }
  }
  
  
  private void updateOldScores(HashSet<Node> set) {
    for(Node node : set) {
      node.updateOldAuthScore();
      node.updateOldHubScore();
    }
  }
}
