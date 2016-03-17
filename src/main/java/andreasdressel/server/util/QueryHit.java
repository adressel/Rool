/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andreasdressel.server.util;

/**
 *
 * @author Andreas Dressel
 */
public class QueryHit {
  
  private final int id;
  private final double score;
  
  public QueryHit(int id, double score) {
    this.id = id;
    this.score = score;
  }
  
  public int getID() {
    return this.id;
  }
  
  public double getScore() {
    return this.score;
  }
}
