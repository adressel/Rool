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
  protected JSONArray toJSONArray(Node[] hits) {
    JSONArray result = new JSONArray();
      for(Node node : hits) {
        
        if(node instanceof HITSNode) {
          JSONObject obj = new JSONObject();
          obj.put("id", node.getID());
          obj.put("hub_score", ((HITSNode)node).getHubScore());
          obj.put("auth_score", ((HITSNode)node).getAuthScore());
          result.add(obj);
        }
      }
    return result;
  }
}
