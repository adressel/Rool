package andreasdressel.hits.util;

import andreasdressel.server.Server;
import andreasdressel.util.Node;
import andreasdressel.server.util.QueryHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Andreas Dressel
 */
public class HITSQueryHandler extends QueryHandler {

  public HITSQueryHandler(Server server) {
    super(server);
  }
  
  @Override
  protected JSONObject toJSONObject(Node[] hits) {
    JSONObject object = new JSONObject();
    
    JSONArray hubScores = new JSONArray(), authScores = new JSONArray();
    for(Node node : hits) {
        
      if(node instanceof HITSNode) {
        JSONObject h = new JSONObject();
        h.put("id", node.getID());
        h.put("hub_score", ((HITSNode)node).getHubScore());
        
        JSONObject a = new JSONObject();
        a.put("id", node.getID());
        a.put("auth_score", ((HITSNode)node).getAuthScore());
          
        hubScores.add(h);
        authScores.add(a);
      }
    }
    object.put("hub_scores", hubScores);
    object.put("auth_scores", authScores);
    
    return object;
  } 
}
