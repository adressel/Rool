package andreasdressel.hits;

import andreasdressel.hits.util.HITSNode;
import andreasdressel.hits.util.HITSQueryHandler;
import andreasdressel.server.Server;
import andreasdressel.server.util.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Dressel
 */
public final class HITSQueryServer extends Server {
  
  private final HashMap<String, HashSet<HITSNode>> invertedIndex;
  private final HashSet<String> stopWords;
  private static final int NUMBER_OF_ITERATIONS = 10;
  
  
  private HITSQueryServer(int port, String filename) {
    super(port, filename);
    
    this.invertedIndex = new HashMap<String, HashSet<HITSNode>>();
    this.stopWords = new HashSet<String>();
    initServer();
    start(new HITSQueryHandler(this));
    
  }
  
  @Override
  public void initServer() {
    // read in the inverted index file, stopwords file and start the server
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Node[] processQuery(String query) {
    if(query == null) {
      System.err.println("Query must not be null. (processQuery HITS)");
      return null;
    }
    
    query = query.replaceAll("[^a-zA-Z0-9]", " ");
    query = query.toLowerCase();
        
    Set<HITSNode> hits = computeHITS(query, NUMBER_OF_ITERATIONS);    
    Node[] result = hits.toArray(new Node[0]);
    
    //@todo: does sorting make any sense here? if yes, how? (two scores)
    
    return result;
  }
  
  private synchronized Set<HITSNode> computeHITS(String query, int iterations) {
    
    // 1. compute base set
    HashSet<HITSNode> seedSet = new HashSet<HITSNode>();
    getBaseSet(query, seedSet);
    
    // 2. extend base set to seed set
    extendToSeedSet(seedSet);
    
    // 3. iteratively calculate hits values, and normalize after each step
    double sumOfSquaredAuthScores = 0, sumOfSquaredHubScores = 0;
    while(iterations > 0) {
      
      for(HITSNode node : seedSet) {
        sumOfSquaredAuthScores += Math.pow(node.calculateNewAuthScore(), 2);
        sumOfSquaredHubScores += Math.pow(node.calculateNewHubScore(), 2);
      }
      
      sumOfSquaredAuthScores = Math.sqrt(sumOfSquaredAuthScores);
      sumOfSquaredHubScores = Math.sqrt(sumOfSquaredHubScores);
      
      for(HITSNode node : seedSet) {
        node.normalizeScores(sumOfSquaredAuthScores, sumOfSquaredHubScores);
      }
      
      iterations--;
      if(iterations > 0) {
        updateOldScores(seedSet);
      }
    }
    
    return seedSet;
  }
  
  
  private void getBaseSet(String query, HashSet<HITSNode> set) {
    // base set has to contain all words from the query (AND)
    // this is one possibile implementation choice (another one would be to allow phrases)
    
    String[] queryElements = query.split(" ");
    int i = 0;
    while(set.isEmpty() && i < queryElements.length) {
      
      if(!this.stopWords.contains(queryElements[i])) {
        if(this.invertedIndex.containsKey(queryElements[i])) {
          set.addAll(this.invertedIndex.get(queryElements[i]));
        }
      }

      i++;
    }
    
    while(i < queryElements.length) {
      
      if(!this.stopWords.contains(queryElements[i])) {
        if(this.invertedIndex.containsKey(queryElements[i])) {
          set.retainAll(this.invertedIndex.get(queryElements[i]));
        }
      }
      
      i++;
    }
  }
  
  private void extendToSeedSet(HashSet<HITSNode> set) {
    for(HITSNode node : set) {
      set.addAll(node.getOutgoingLinks());
      set.addAll(node.getIncomingLinks());
    }
  }
  
  
  private void updateOldScores(HashSet<HITSNode> set) {
    for(HITSNode node : set) {
      node.updateOldAuthScore();
      node.updateOldHubScore();
    }
  }
}
