import java.io.IOException;
import java.net.Socket;

/**
 * Wrapper class for server main-method and for multiple clients that can communicate via independent
 * server threads.
 */
public class App {
  private final static int PORT_NUMBER = 9000;
  private final static String SERVER_DOMAIN = "localhost";

  public static class MainServer {

    public static void main(String[] args) {
      try {
        Server theServer = new Server(PORT_NUMBER);
        boolean run = true;
        while (run) {
          Socket serverSocket = theServer.serverAccept();
          System.out.println("A total of " + theServer.getNumConnections() +
              " client(s) is connected.");

          ServerThread st = new ServerThread(serverSocket);
          st.start();
          if(theServer.getNumConnections() <= 0) run = false;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  public static class MainClient {
    public static void main(String[] args) {
      try {
        new Client(SERVER_DOMAIN, PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static class MainClient2 {
    public static void main(String[] args) {
      try {
        new Client(SERVER_DOMAIN, PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static class MainClient3 {
    public static void main(String[] args) {
      try {
        new Client(SERVER_DOMAIN, PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
