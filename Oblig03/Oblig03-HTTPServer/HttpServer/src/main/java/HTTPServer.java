import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * HTTP server that listens for incoming connections on port 8080.
 * When a connection is accepted, the server sends a welcome message to the client
 * and logs the headers received from the client.
 *
 * @version 1.0
 */
public class HTTPServer {
  public static void run() throws Exception {
    try (ServerSocket server = new ServerSocket(8080)) {
      System.out.println("Waiting for a connection to localhost:8080...");
      while(server.isBound()) {
        try (Socket socket = server.accept()) {
          sendWelcomeMessage(socket);
          logHeadersFromClient(socket);
          server.close();
        }
      }
    }
  }

  /**
   * Sends a welcome message to the client.
   *
   * @param socket the socket representing the client connection
   * @throws Exception if an error occurs while sending the message
   */
  private static void sendWelcomeMessage(Socket socket) throws Exception {
    String httpResponse = """
                            HTTP/1.1 200 OK\r
                            \r
                            
                            <h1>Welcome!</h1>
                            """;
    socket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Logs the headers received from the client.
   *
   * @param socket the socket representing the client connection
   * @throws Exception if an error occurs while logging the headers
   */
  private static void logHeadersFromClient(Socket socket) throws Exception {
    String direction = """
              Header from client is:\040
              
              """;
    StringBuilder sb = new StringBuilder(direction + "\n<ul>");
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String rLine;
    while (!(rLine = in.readLine()).equals("")) {
      String input = "<li>" + rLine + "</li>";
      sb.append(input);
    }
    sb.append("</ul>");
    String out = sb.toString();

    socket.getOutputStream().write(out.getBytes(StandardCharsets.UTF_8));
    in.close();

  }

  /**
   * Entry point of the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      HTTPServer.run();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}