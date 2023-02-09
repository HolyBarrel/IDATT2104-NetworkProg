import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

/**
 * Represents a server thread handling communication with a client via a socket connection.
 * This class extends the {@code Thread} class and overrides its {@code run()} method.
 * client. The input must be in the format of an arithmetic expression with two integer values and
 * an operator (+, -, *, /).
 *
 * @version 1.0
 */
public class ServerThread extends Thread {
  private final Socket socket;

  public ServerThread(Socket socket){
    this.socket = socket;
  }

  /**
   * If this thread was constructed using a separate {@code Runnable} run object, then that {@code
   * Runnable} object's {@code run} method is called; otherwise, this method does nothing and
   * returns.
   * <p>
   * Subclasses of {@code Thread} should override this method.
   *
   */
  @Override
  public void run() {
    try {
      retrieveClientActions();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void retrieveClientActions() throws IOException {
    InputStreamReader in;
    BufferedReader reader = null;
    PrintWriter writer = null;
    try {
      in = new InputStreamReader(socket.getInputStream());
      reader = new BufferedReader(in);
      writer = new PrintWriter(socket.getOutputStream(), true);

      writer.println("Connection to socket: " + socket.getPort() + " is open.");
      String line = reader.readLine();
      while (line != null) {

        double result = 0;

        String[] inputElements = line.split("(?<=[-+*/])|(?=[-+*/])");

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

        System.out.println("A client wrote: " + line);

        writer.println("Result: " + line + " = " + result);
        line = reader.readLine();
      }
    }catch (SocketException s) {
      System.out.println("A client lost its connection");
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
    finally {
      Objects.requireNonNull(reader).close();
      Objects.requireNonNull(writer).close();
    }
  }

}
