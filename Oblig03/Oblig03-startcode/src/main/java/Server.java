import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
  private ServerSocket serverSocket;
  private Socket connection;
  private BufferedReader reader;
  private PrintWriter writer;

  public Server(int portNumber) throws IOException {
    this.serverSocket = new ServerSocket(portNumber);
    this.connection = serverSocket.accept();
    openCommunication();
    retrieveClientActions();
    close();
  }

  private void openCommunication() throws IOException {
    InputStreamReader in = new InputStreamReader(connection.getInputStream());
    this.reader = new BufferedReader(in);
    this.writer = new PrintWriter(connection.getOutputStream(), true);
    writer.println("Connected to PORT: " + connection.getRemoteSocketAddress());
  }

  private void retrieveClientActions() throws IOException {
    String line = reader.readLine();
    while (line != null) {
      System.out.println("A client wrote: " + line);
      writer.println("You wrote: " + line);
      line = reader.readLine();
    }
  }

  private void close() throws IOException {
    writer.close();
    reader.close();
    connection.close();
  }
}
