import java.io.IOException;
import java.net.*;

/**
 * The Server implements a server that listens on a specified port number.
 * It waits to accept connections from clients. The number of connections is tracked.
 */
public class Server {
  private ServerSocket serverSocket;
  private int numConnections;

  /**
   * Constructs a new Server object having a specified port number.
   *
   * @param portNumber the port number to listen on for incoming connections
   * @throws IOException if an I/O error occurs when opening the server socket
   */
  public Server(int portNumber) throws IOException {
    this.serverSocket = new ServerSocket(portNumber);
    this.numConnections = 0;
  }

  /**
   * Accepts incoming connections from clients and returns the corresponding `Socket` object.
   * The number of connections is incremented.
   *
   * @return the `Socket` object for the newly accepted connection
   * @throws IOException if an I/O error occurs when accepting a connection
   */
  public Socket serverAccept() throws IOException {
    Socket s = serverSocket.accept();
    increment();
    return s;
  }

  /**
   * Increments the number of connections.
   */
  private void increment() {
    numConnections++;
  }

  /**
   * Returns the number of connections.
   *
   * @return the number of connections
   */
  public int getNumConnections() {
    return numConnections;
  }
}
