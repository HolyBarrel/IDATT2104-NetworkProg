import java.io.IOException;

public class App {
  private final static int PORT_NUMBER = 2222;

  public static class MainServer {
    public static void main(String[] args) {
      try {
        new Server(PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  public static class MainClient {
    public static void main(String[] args) {
      try {
        new Client("localhost", PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
