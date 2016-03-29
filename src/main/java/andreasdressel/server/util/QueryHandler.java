package andreasdressel.server.util;

import andreasdressel.util.Node;
import andreasdressel.server.Server;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Andreas Dressel
 */
public abstract class QueryHandler implements HttpHandler {
  
  private final Server server;
  
  public QueryHandler(Server server) {
    this.server = server;
  }

  public void handle(HttpExchange exchange) throws IOException {

    String requestMethod = exchange.getRequestMethod().toUpperCase();

    if (requestMethod.equals("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, 0);

      //Headers requestHeaders = exchange.getRequestHeaders();
      String path = exchange.getRequestURI().getPath();

      // Test to make sure the URL is properly formed,
      if ("/search".equals(path)) {
        String query = null;
        String elements[] = exchange.getRequestURI().getQuery().split("&");

        for (String element : elements) {
          String name = element.split("=")[0];
          String val = URLDecoder.decode(element.split("=")[1], "UTF-8");
          if ("q".equals(name)) {
            query = val;
          }
        }

        Node[] hits = this.server.processQuery(query);

        // encode the Nodes in a JSON array
        JSONArray hitarray = toJSONArray(hits);

        JSONObject resultObj = new JSONObject();
        resultObj.put("hits", hitarray);

        // Emit the text
        StringWriter out = new StringWriter();
        resultObj.writeJSONString(out);

        //OutputStream responseBody = exchange.getResponseBody();
        exchange.getResponseBody().write((out.toString()).getBytes());
      }
      exchange.getResponseBody().close();
    }
  }
  
  protected abstract JSONArray toJSONArray(Node[] hits);
}
