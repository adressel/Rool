package andreasdressel.hits;

import andreasdressel.hits.util.HITSGraphParser;
import andreasdressel.hits.util.HITSNode;
import andreasdressel.hits.util.HITSQueryHandler;
import andreasdressel.server.Server;
import andreasdressel.util.Node;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import andreasdressel.util.Stopwords;

/**
 *
 * @author Andreas Dressel
 */
public final class HITSQueryServer extends Server {
  
  private static int NUMBER_OF_ITERATIONS = 0;
  
  private final HashMap<Integer, HITSNode> nodes;
  private final HashMap<String, HashSet<HITSNode>> invertedIndex;
  private final HashSet<String> stopWords;
  
  
  private HITSQueryServer(int port, String configFileName) {
    super(port, configFileName);
    
    this.nodes = new HashMap<Integer, HITSNode>();
    this.invertedIndex = new HashMap<String, HashSet<HITSNode>>();
    this.stopWords = Stopwords.getInstance().getStopwords();
    initServer();
    start(new HITSQueryHandler(this));
    
  }
  
  @Override
  public void initServer() {
    /* 
    Configuration file format:
    
    -s <stopwordsfile>   // this should be done in the main config file
    -g <graphfile>
    -i <invertedindexfile>
    -p <precision>
    */
    
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(new InputStreamReader(
              new FileInputStream(new File(this.configFileName))));
      
      String line = null;
      while((line = reader.readLine()) != null) {
        String filename = line.substring(line.indexOf(" ") + 1);
        if(line.startsWith("-s")) {
          Stopwords.init(filename);
        } else if(line.startsWith("-g")) {
          HITSGraphParser.parseGraph(filename, this.nodes);
        } else if(line.startsWith("-i")) {
          parseInvertedIndexFile(filename);
        } else if(line.startsWith("-p")) {
          NUMBER_OF_ITERATIONS = Integer.parseInt(filename);
        } else {
          System.err.println("Malformed configuration file (HITSQueryServer).");
        }
      }
      
    } catch(FileNotFoundException ex) {
      System.err.println("HITS inverted index file not found.");
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
    // one option would be to return two lists with the sorted hubs scores, 
    // and the sorted auth scores respectively
    
    return result;
  }
  
  private synchronized Set<HITSNode> computeHITS(String query, int iterations) {
    
    // 1. compute base set
    HashSet<HITSNode> seedSet = new HashSet<HITSNode>();
    getBaseSet(query, seedSet);
    
    // 2. extend base set to seed set
    extendToSeedSet(seedSet);
    
    //@todo: reset all nodes into their original state! (all scores to 1)
    
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
  
  private void parseInvertedIndexFile(String filename) {
    BufferedReader reader = null;
    
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
      
      String line = null;
      while((line = reader.readLine()) != null) {
        int index = line.indexOf("\t");
        String word = line.substring(0, index);
        String[] ids = line.substring(index + 1).split(" ");

        HashSet<HITSNode> elements = this.invertedIndex.containsKey(word) ?
                this.invertedIndex.get(word) : new HashSet<HITSNode>();

        for(String s : ids) {
          int id = Integer.parseInt(s);
          HITSNode node = this.nodes.get(id);
          if(node == null) {
            System.err.println("Warning: Node with id <" + id + "> is not in the graph file "
                    + "but referred to in the inverted index file.");
          } else {
            elements.add(node);
          }
        }
        this.invertedIndex.put(word, elements);
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
