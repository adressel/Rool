package andreasdressel.server;

import andreasdressel.server.util.QueryHit;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 *
 * @author Andreas Dressel
 */

public abstract class Server {
  protected final int port;
  protected final String filename;
  
  /**
   * 
   * 
   * @param port
   * @param filename
   */
  protected Server(int port, String filename) {
    this.port = port;
    this.filename = filename;
    
    //initServer(filename);
  }

  /**
   * 
   * 
   * @throws java.io.IOException
   */
  public void start() throws IOException {
    
    HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
    server.createContext("/", new QueryHandler());
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    
    System.out.println("Server is listening on port " + this.port);
  }
  
  /**
   * Jetty callback
   */
  public void get() {
    //processQuery(req.get("query"));
  }

  /**
   * 
   */
  public abstract void initServer();
  
  /**
   * 
   * @param query
   * @return 
   */
  public abstract List<QueryHit> processQuery(String query);
  
  
  class QueryHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
      
      String requestMethod = exchange.getRequestMethod().toUpperCase();
      
      if(requestMethod.equals("GET")) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        
        //Headers requestHeaders = exchange.getRequestHeaders();
        String path = exchange.getRequestURI().getPath();

        // Test to make sure the URL is properly formed,
        if("/search".equals(path)) {
          String query = null;
          String elements[] = exchange.getRequestURI().getQuery().split("&");
          
          for(String element : elements) {
            String name = element.split("=")[0];
            String val = URLDecoder.decode(element.split("=")[1], "UTF-8");
            if ("q".equals(name)) {
              query = val;
            }
          }
          
          List<QueryHit> hits = processQuery(query);

          
          // Now we can handle the QueryHit JSON encoding
          JSONArray hitarray = new JSONArray();
          for(QueryHit q : hits) {
            JSONObject hitObj = new JSONObject();
            hitObj.put("id", q.getID());
            hitObj.put("score", q.getScore());
            hitarray.add(hitObj);
          }
          
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
  }
}