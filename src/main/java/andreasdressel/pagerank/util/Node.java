package andreasdressel.pagerank.util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Dressel
 */
public class Node {
  
  private double rank, weightedRank;
  private long numberOfOutgoingEdges; // a long should be enough, otherwise use BigInteger
  private final HashSet<Node> incomingEdges;
  
  /**
   * Creates a new Node object. A Node is a part of a graph and can as such be assigned a rank. To 
   * correctly initialize a graph for the PageRank algorithm implemented in 
   * andreasdressel.pagerank.PageRank.java, each Node must have these properties:
   * - the initial rank is set to 1/numberOfNodes. numberOfNodes is the total number of Nodes in 
   *   the graph.
   * - the number of outgoing edges is correctly set. The number of outgoing edges for a Node N 
   *   is equal to the number of all edges from Node N to any other Node in the graph.
   * - the incoming edges are correctly populated. IncomingEdges of a Node N has to contain all 
   *   Nodes K for which an edge from K to N exists.
   * 
   * PageRank is an algorithm presented in:
   * Source: Sergey Brin and Lawrence Page: "The Anatomy of a Large-Scale Hypertextual 
   * Web Search Engine" (1998)
   * 
   * @param initialRank The initial PageRank 1/numberOfNodes.
   * @param numberOfOutgoingEdges The number of outgoing edges from this Node.
   */
  public Node(double initialRank, long numberOfOutgoingEdges) {
    this.rank = initialRank;
    this.weightedRank = 0;
    this.numberOfOutgoingEdges = numberOfOutgoingEdges;
    this.incomingEdges = new HashSet<Node>();
  }
  
  public void addIncomingEdge(Node node) {
    this.incomingEdges.add(node);
  }
  
  public Set<Node> getIncomingEdges() {
    return this.incomingEdges;
  }
  
  public void addOutgoingEdge() {
    this.numberOfOutgoingEdges += 1;
  }
  
  public void setRank(double newRank) {
    this.rank = newRank;
  }
  
  public double getRank() {
    return this.rank;
  }
  
  public double getWeightedRank() {
    return this.weightedRank;
  }
  
  public void updateWeightedRank(int graphSize) {
    if(this.numberOfOutgoingEdges > 0) {
      this.weightedRank = this.rank / this.numberOfOutgoingEdges;
    } else {
      this.weightedRank = this.rank / graphSize;
    }
  }
  
  public boolean hasOutgoingEdges() {
    return this.numberOfOutgoingEdges > 0;
  }

}
