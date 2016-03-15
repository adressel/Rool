/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andreasdressel.hits.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Dressel
 */
public class Node {
  
  private final List<Node> outgoingLinks, incomingLinks;
  private double hubScore, authScore, hubScoreOld, authScoreOld;
  
  
  public Node() {
    this.outgoingLinks = new ArrayList<Node>();
    this.incomingLinks = new ArrayList<Node>();
    
    this.hubScore = 1;
    this.authScore = 1;
    this.hubScoreOld = 1;
    this.authScoreOld = 1;
  }
  
  public double calculateNewAuthScore() {
    this.authScore = 0;
    for(Node node : this.incomingLinks) {
      this.authScore += node.hubScoreOld;
    }
    return this.authScore;
  }
  
  public void updateOldAuthScore() {
    this.authScoreOld = this.authScore;
  }
  
  public double calculateNewHubScore() {
    this.hubScore = 0;
    for(Node node : this.outgoingLinks) {
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
}
