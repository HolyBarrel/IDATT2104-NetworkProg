import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;


public class Client {
  private Socket connection;
  private BufferedReader reader;
  private PrintWriter writer;

  public Client(String domain, int portNumber) throws IOException {
    connection = new Socket(domain, portNumber);
    openCommunication();
    retrieveServerCom();
    close();
  }

  private void openCommunication() throws IOException {
    InputStreamReader in = new InputStreamReader(connection.getInputStream());
    this.reader = new BufferedReader(in);
    this.writer = new PrintWriter(connection.getOutputStream(), true);
    System.out.println(reader.readLine());
  }

  private void retrieveServerCom() throws IOException {
    Scanner scan = new Scanner(System.in);
    String line = scan.nextLine();
    while (!line.equals("")) {
      writer.println(line);
      String response = reader.readLine();
      System.out.println("From server: " + response);
      line = scan.nextLine();
    }
    scan.close();
  }

  public void close() throws IOException {
    System.out.println("Closing down connection on port: "
        + getRemoteAddress());
    reader.close();
    writer.close();
    connection.close();
  }

  private SocketAddress getRemoteAddress() {
    return this.connection.getRemoteSocketAddress();
  }
}
