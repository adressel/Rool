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
  
  public void setNewAuthScore() {
    
    
  }
  
  public void updateOldAuthScore() {
    this.authScoreOld = this.authScore;
  }
  
  public void setNewHubScore() {
    
  }
  
  public void updateOldHubScore() {
    this.hubScoreOld = this.hubScore;
  }
}
