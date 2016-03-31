package andreasdressel.hits.util;

import andreasdressel.util.Node;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Dressel
 */
public class HITSNode extends Node {
  
  private final Set<HITSNode> outgoingLinks, incomingLinks;
  private double hubScore, authScore, hubScoreOld, authScoreOld;
  
  
  public HITSNode(int id) {
    super(id);
    
    this.outgoingLinks = new HashSet<HITSNode>();
    this.incomingLinks = new HashSet<HITSNode>();
    
    this.hubScore = 1;
    this.authScore = 1;
    this.hubScoreOld = 1;
    this.authScoreOld = 1;
  }
  
  public double calculateNewAuthScore(HashSet<HITSNode> seedSet) {
    this.authScore = 0;
    for(HITSNode node : this.incomingLinks) {
      if(!seedSet.contains(node) || node.id == this.id) continue;
      
      this.authScore += node.hubScoreOld;
    }
    return this.authScore;
  }
  
  public void updateOldAuthScore() {
    this.authScoreOld = this.authScore;
  }
  
  public double calculateNewHubScore(HashSet<HITSNode> seedSet) {
    this.hubScore = 0;
    for(HITSNode node : this.outgoingLinks) {
      if(!seedSet.contains(node) || node.id == this.id) continue;
      
      this.hubScore += node.authScoreOld;
    }
    return this.hubScore;
  }
  
  public void updateOldHubScore() {
    this.hubScoreOld = this.hubScore;
  }
  
  public void normalizeScores(double sumOfSquaredAuthScores, double sumOfSquaredHubScores) {
    if(sumOfSquaredAuthScores == 0.0 || sumOfSquaredHubScores == 0.0) {
      if(sumOfSquaredAuthScores == 0.0) System.err.println("Error: sumOfSquaredAuthScores == 0.0");
      if(sumOfSquaredHubScores == 0.0) System.err.println("Error: sumOfSquaredHubScores == 0.0");
      return;
    }
    this.authScore /= sumOfSquaredAuthScores;
    this.hubScore /= sumOfSquaredHubScores;
  }
  
  public Set<HITSNode> getOutgoingLinks() {
    return this.outgoingLinks;
  }
  
  public void addOutgoingLink(HITSNode node) {
    this.outgoingLinks.add(node);
  }
  
  public void addIncomingLink(HITSNode node) {
    this.incomingLinks.add(node);
  }
  
  public Set<HITSNode> getIncomingLinks() {
    return this.incomingLinks;
  }
  
  public double getHubScore() {
    return this.hubScore;
  }
  
  public double getAuthScore() {
    return this.authScore;
  }
  
  public void setScoresToInitialState() {
    this.hubScore = 1.0;
    this.authScore = 1.0;
    this.hubScoreOld = 1.0;
    this.authScoreOld = 1.0;
  }
}
