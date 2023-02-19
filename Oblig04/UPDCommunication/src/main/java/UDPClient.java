import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDPClient class to serve as a simple client using UDP for communication with a server.
 * Accepts the hostname as a command line argument, reads an expression on the format 'x+y'
 * from the console, then sends it to the server.
 * It waits for the result from the server and prints it to the console.
 */
public class UDPClient {

  public static void main(String[] args) throws IOException {

    if (args.length != 1) {
      System.out.println("Compile with: java UDPClient 'host name'");
      return;
    }

    // Create byte arrays for incoming and outgoing packets
    byte[] bufferIn = new byte[256];
    byte[] bufferOut = new byte[256];

    System.out.println("Please enter an expression on the format "
        + "'x+y' to receive result from server: ");

    // Loop indefinitely, reading expressions from the console and sending them to the server
    while(true) {
      try (DatagramSocket socket = new DatagramSocket();
      ) {
        // Get the InetAddress for the specified hostname
        InetAddress address = InetAddress.getByName(args[0]);

        // Read a line of text from the console
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String line = userInput.readLine();


        bufferOut = line.getBytes();
        DatagramPacket packet = new DatagramPacket(bufferOut, bufferOut.length, address, 5000);
        // Send packet to the server
        socket.send(packet);

        // Receive a packet from the server
        DatagramPacket packetIn = new DatagramPacket(bufferIn, bufferIn.length);
        socket.receive(packetIn);

        // Convert the packet data to a string and print it to the console
        String str = new String(packetIn.getData(), 0, packetIn.getLength());
        System.out.println(str);
      }
    }


  }

  }
