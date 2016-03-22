package andreasdressel.pagerank;

import andreasdressel.pagerank.util.PageRankNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Dressel
 */
public class PageRank {
  
  /**
   * Calculates the PageRank for a given graph of nodes. The PageRank of a node A is defined as:
   * 
   *   PR(A) = (1-d) / #nodes + d * SUM( PR(Ti)/C(Ti) )
   * 
   * - d denotes a damping factor that can be chosen between 0 and 1.
   * - PR(Ti) is the PageRank of node Ti.
   * - C(Ti) is the number of outgoing edges from node Ti.
   * 
   * This method computes a certain number of iterations of the PageRank algorithm before 
   * terminating.
   * 
   * Source: Sergey Brin and Lawrence Page: "The Anatomy of a Large-Scale Hypertextual 
   * Web Search Engine" (1998)
   * 
   * @param graph A graph of Nodes for which to compute the PageRank.
   * @param dValue A damping factor between 0 and 1.
   * @param maxIterations 
   */
  public static void calculatePageRank(List<PageRankNode> graph, double dValue, int maxIterations) {
    if(graph == null || graph.isEmpty()) return;
    if(dValue == 0) return;
    if(dValue < 0 || dValue > 1) {
      System.err.println("Illegal argument. 0 <= dValue <= 1");
      return;
    }
    
    List<PageRankNode> noOutgoingEdges = getNodesWithoutOutgoingEdges(graph);
    
    while(maxIterations > 0) {
      calculateWeightedRanks(graph);
      double rankAdjustment = calculateRankAdjustment(noOutgoingEdges);

      for(PageRankNode node : graph) {
        
        double newPR = rankAdjustment;
        for(PageRankNode incomingEdge : node.getIncomingEdges()) {
          newPR += incomingEdge.getWeightedRank();
        }

        newPR = dValue * newPR + ((1 - dValue) / graph.size());
        node.setRank(newPR);
      }
      
      maxIterations--;
    }
  }
  
  
  /**
   * Calculates the PageRank for a given graph of nodes. The PageRank of a node A is defined as:
   * 
   *   PR(A) = (1-d) / #nodes + d * SUM( PR(Ti)/C(Ti) )
   * 
   * - d denotes a damping factor that can be chosen between 0 and 1.
   * - PR(Ti) is the PageRank of node Ti.
   * - C(Ti) is the number of outgoing edges from node Ti.
   * 
   * This method computes the PageRank of each node repeatedly until a certain level of 
   * accuracy is achieved, and the difference of the PageRank values for every node before
   * and after one iteration is not greater than maxChange.
   * 
   * PageRank is an algorithm presented in:
   * Source: Sergey Brin and Lawrence Page: "The Anatomy of a Large-Scale Hypertextual 
   * Web Search Engine" (1998)
   * 
   * @param graph A graph of Nodes for which to compute the PageRank.
   * @param dValue A damping factor between 0 and 1.
   * @param maxChange The maximum deviation in PageRanks from one iteration to the next for every
   * node in the graph.
   */
  public static void calculatePageRank(List<PageRankNode> graph, double dValue, double maxChange) {
    if(graph == null || graph.isEmpty()) return;
    if(dValue == 0) return;
    if(dValue < 0 || dValue > 1) {
      System.err.println("Illegal argument. 0 <= dValue <= 1");
      return;
    }
    
    List<PageRankNode> noOutgoingEdges = getNodesWithoutOutgoingEdges(graph);
    
    boolean ranksChangedSignificantly;
    do {      
      ranksChangedSignificantly = false;
      
      calculateWeightedRanks(graph);
      double rankAdjustment = calculateRankAdjustment(noOutgoingEdges);
    
      for(PageRankNode node : graph) {
      
        double newPR = rankAdjustment;
        for(PageRankNode incomingEdge : node.getIncomingEdges()) {
          newPR += incomingEdge.getWeightedRank();
        }
      
        newPR = dValue * newPR + ((1 - dValue) / graph.size());
        
        if(Math.abs(node.getRank() - newPR) > maxChange) {
          ranksChangedSignificantly = true;
        }
        node.setRank(newPR);
      }
      
    } while(ranksChangedSignificantly);
  }
  
  
  private static void calculateWeightedRanks(List<PageRankNode> graph) {
    for(PageRankNode node : graph) {
      node.updateWeightedRank(graph.size());
    }
  }
  
  private static List<PageRankNode> getNodesWithoutOutgoingEdges(List<PageRankNode> graph) {
    List<PageRankNode> result = new ArrayList<PageRankNode>();
    for(PageRankNode node : graph) {
      if(!node.hasOutgoingEdges()) {
        result.add(node);
      }
    }
    return result;
  }
  
  private static double calculateRankAdjustment(List<PageRankNode> noOutgoingEdges) {
    double result = 0.0;
    for(PageRankNode node : noOutgoingEdges) {
      result += node.getWeightedRank();
    }
    return result;
  }
}
