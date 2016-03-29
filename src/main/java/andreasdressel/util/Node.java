package andreasdressel.util;

/**
 *
 * @author Andreas Dressel
 */
public abstract class Node {
  
  private final int id;
  
  public Node(int id) {
    this.id = id;
  }
  
  public int getID() {
    return this.id;
  }
  
}
