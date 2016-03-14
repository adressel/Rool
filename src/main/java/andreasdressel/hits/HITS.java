/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andreasdressel.hits;

import andreasdressel.hits.util.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

/**
 *
 * @author Andreas Dressel
 */
public class HITS {
  
  private final HashMap<String, SortedSet<Integer>> invertedIndex;
  
  
  private HITS() {
    this.invertedIndex = new HashMap<String, SortedSet<Integer>>();
    
    
  }
  
  // how to handle multiple word queries? simply AND, or phrase queries?
  
  public synchronized List<Node> computeHITS(String query, int iterations) {
    if(query == null) {
      System.err.println("Query must not be null. (computeHITS)");
      return null;
    }
    
    // this implementation is not case-sensitive
    query = query.toLowerCase();
    
    // handle stop words
    
    
    // 1. compute base set
    List<Node> seedSet = new ArrayList<Node>();
    getBaseSet(query, seedSet);
    
    // 2. extend base set to seed set
    extendToSeedSet(seedSet);
    
    // 3. iteratively calculate hits values, and normalize after each step
    while(iterations > 0) {
      
      for(Node node : seedSet) {
        node.setNewAuthScore();
        node.setNewHubScore();
      }
      
      normalizeScores(seedSet);
      
      iterations--;
      if(iterations > 0) {
        updateOldScores(seedSet);
      }
    }
    
    return seedSet;
  }
  
  
  private void getBaseSet(String query, List<Node> list) {
    // base set has to contain all words from the query (AND)
    
    
  }
  
  private void extendToSeedSet(List<Node> list) {
    
  }
  
  private void normalizeScores(List<Node> list) {
    
  }
  
  private void updateOldScores(List<Node> list) {
    for(Node node : list) {
      node.updateOldAuthScore();
      node.updateOldHubScore();
    }
  }
  
}
