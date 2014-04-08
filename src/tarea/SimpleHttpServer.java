package tarea;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
*/
public class SimpleHttpServer {

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/", new Handler1());
    server.setExecutor(null); // creates a default executor
    server.start();
  }

  static class Handler1 implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
      String response = "bla segunda prueba GIT";
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
      
      //comentarioooooo
    }
  }

}