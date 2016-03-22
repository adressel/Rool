package andreasdressel.pagerank.main;

import andreasdressel.pagerank.PageRank;
import andreasdressel.pagerank.io.GraphParser;
import andreasdressel.pagerank.io.NodeDictionary;
import andreasdressel.pagerank.util.PageRankNode;
import java.util.List;

/**
 *
 * @author Andreas Dressel
 */
public class Main {
  
  
  
  public static void main(String[] args) {
    if(args.length != 5) {
      System.err.println("Malformed input. Usage:\n Test <dValue> (-i <numberOfIterations> | "
          + "-converge <maxChange>) <inputFileName> <outputFileName>");
      return;
    }
    
    List<PageRankNode> graph = GraphParser.initializeGraph(args[3]);
    double dValue = Double.parseDouble(args[0]);
    
    if(args[1].equals("-i")) {
      PageRank.calculatePageRank(graph, dValue, Integer.parseInt(args[2]));
    } else {
      PageRank.calculatePageRank(graph, dValue, Double.parseDouble(args[2]));
    }
    
    NodeDictionary.getInstance().writeRanksToFile(args[4]);
  }
}
