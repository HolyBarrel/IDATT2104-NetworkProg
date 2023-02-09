import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;


/**
 *  Client class that provides a client-side implementation of a socket communication.
 *  It creates a {@link Socket} connection to a server located at the specified domain and port number,
 *  and opens a communication channel for reading and writing to the server.
 *  The client reads a line from the user input and sends it to the server, and displays the response
 *  received from the server. The communication is closed after the user stops entering lines of text.
 *
 *  @version 1.0
 */
public class Client {
  private Socket connection;
  private BufferedReader reader;
  private PrintWriter writer;

  /**
   * Constructor that creates a {@link Socket} connection to a server at the specified domain and port number,
   * opens a communication channel for reading and writing, retrieves and displays the response from the server,
   * and closes the communication.
   *
   * @param domain the domain name or IP address of the server
   * @param portNumber the port number to connect to the server
   * @throws IOException if an I/O error occurs when opening the communication channel
   */
  public Client(String domain, int portNumber) throws IOException {
    connection = new Socket(domain, portNumber);
    openCommunication();
    retrieveServerCom();
    close();
  }

  /**
   * Helper method that opens the communication channel for reading and writing.
   *
   * @throws IOException if an I/O error occurs when opening the communication channel
   */
  private void openCommunication() throws IOException {
    InputStreamReader in = new InputStreamReader(connection.getInputStream());
    this.reader = new BufferedReader(in);
    this.writer = new PrintWriter(connection.getOutputStream(), true);
    System.out.println(reader.readLine());
  }

  /**
   * Helper method that retrieves and displays the response from the server.
   *
   * @throws IOException if an I/O error occurs when reading from the server
   */
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

  /**
   * Helper method that closes the communication channel and the {@link Socket} connection.
   * @throws IOException if an I/O error occurs when closing the communication channel
   *                      or the {@link Socket} connection
   */
  public void close() throws IOException {
    System.out.println("Closing down connection to: "
        + connection.getPort());
    reader.close();
    writer.close();
    connection.close();
  }
}
