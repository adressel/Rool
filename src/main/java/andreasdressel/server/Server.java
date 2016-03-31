package andreasdressel.server;

import andreasdressel.util.Node;
import andreasdressel.server.util.QueryHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;

/**
 *
 * @author Andreas Dressel
 * partial credit: EECS 485 UMich
 */

public abstract class Server {
  protected final int port;
  protected final String configFileName;
  
  /**
   * 
   * 
   * @param port
   * @param configFileName
   */
  protected Server(int port, String configFileName) {
    this.port = port;
    this.configFileName = configFileName;
    
    //initServer(filename);
  }

  /**
   * 
   * 
   * @param handler
   */
  public void start(QueryHandler handler) {
    
    try {
      HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
      server.createContext("/", handler);
      server.setExecutor(Executors.newCachedThreadPool());
      server.start();
    
      System.out.println("Server is listening on port " + this.port);
    } catch(IOException ex) {
      System.err.println("Error creating InetSocketAddress when starting the server");
      ex.printStackTrace();
    }
  }

  /**
   * 
   */
  protected abstract void initServer();
  
  /**
   * 
   * @param query
   * @return 
   */
  public abstract Node[] processQuery(String query);
}