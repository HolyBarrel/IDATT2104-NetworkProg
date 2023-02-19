import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP server that listens for incoming packets on port 5000 and responds with the result
 * of the mathematical expression sent by the client in the packet.
 */
public class UDPServer {
  private DatagramSocket socket;
  private BufferedReader in;
  private DatagramPacket packet;
  byte[] bufferIn = new byte[256];
  byte[] bufferOut = new byte[256];

  /**
   * Initializes a new UDP server with a DatagramSocket listening on port 5000.
   * Waits for incoming packets containing a mathematical expression and responds
   * with the result of the expression.
   *
   * @throws IOException if an I/O error occurs.
   */
  public UDPServer() throws IOException {
    socket = new DatagramSocket(5000);
    System.out.println("Server is waiting for UDP packets...");
    while(true) {
      packet = new DatagramPacket(bufferIn, bufferIn.length);
      socket.receive(packet);

      String packetString = new String(packet.getData(), 0, packet.getLength());

      String result = packetString + " = " + compute(packetString);

      System.out.println(result);

      bufferOut = result.getBytes();

      InetAddress address = packet.getAddress();
      int port = packet.getPort();
      packet = new DatagramPacket(bufferOut, bufferOut.length, address, port);
      socket.send(packet);
    }
  }

  /**
   * Computes the result of a mathematical expression of the form
   * "x operator y" where x and y are numbers and operator is one of the
   * supported mathematical operators: +, -, *, and /.
   *
   * @param input the mathematical expression to compute.
   * @return the result of the mathematical expression.
   */
  private String compute(String input) {
    String[] inputElements = input.split("(?<=[-+*/])|(?=[-+*/])");

    double result = 0;

    double firstNum;
    double secondNum;
    try {
      firstNum = Double.parseDouble(inputElements[0]);
      secondNum = Double.parseDouble(inputElements[2]);
      switch (inputElements[1]) {
        case "+" -> result = firstNum + secondNum;
        case "-" -> result = firstNum - secondNum;
        case "*" -> result = firstNum * secondNum;
        case "/" -> {
          if (secondNum == 0)
            throw new NumberFormatException(
                "The second number was zero (0). Cannot perform division with zero as denominator! "
                    + "Please try again");
          result = firstNum / secondNum;
        }
        default -> System.out.println(
            "The expression was not compatible to the expected values. "
                + "Please input an expression with integer values 'val1' 'operator' 'val2'");
      }
    } catch (NumberFormatException n) {
      System.out.println(n.getMessage());
    }
    return String.valueOf(result);
  }

  /**
   * Starts the UDP server by creating a new instance of UDPServer.
   * @param args the command-line arguments. (IMPORTANT! Edit run configurations to run with the
   *             argument 'host name')
   */
  public static void main(String[] args) {
    try {
      new UDPServer();
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
