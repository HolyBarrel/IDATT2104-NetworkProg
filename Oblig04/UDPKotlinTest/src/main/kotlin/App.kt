import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class App {
    /**
     * UDPClient class to serve as a simple client using UDP for communication with a server.
     * Accepts the hostname as a command line argument, reads an expression on the format 'x+y'
     * from the console, then sends it to the server.
     * It waits for the result from the server and prints it to the console.
     */
    object UDPClient {

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            if (args.size != 1) {
                println("Compile with: java UDPClient 'host name'")
                return
            }

            // Create byte arrays for incoming and outgoing packets
            val bufferIn = ByteArray(256)
            var bufferOut = ByteArray(256)

            println("Please enter an expression on the format " + "'x+y' to receive result from server: ")

            // Loop indefinitely, reading expressions from the console and sending them to the server
            while (true) {
                DatagramSocket().use { socket ->
                    // Get the InetAddress for the specified hostname
                    val address = InetAddress.getByName(args[0])

                    // Read a line of text from the console
                    val userInput = BufferedReader(InputStreamReader(System.`in`))
                    val line = userInput.readLine()


                    bufferOut = line.toByteArray()
                    val packet = DatagramPacket(bufferOut, bufferOut.size, address, 5000)
                    // Send packet to the server
                    socket.send(packet)

                    // Receive a packet from the server
                    val packetIn = DatagramPacket(bufferIn, bufferIn.size)
                    socket.receive(packetIn)

                    // Convert the packet data to a string and print it to the console
                    val str = String(packetIn.data, 0, packetIn.length)
                    println(str)
                }
            }


        }

    }


    /**
     * UDP server that listens for incoming packets on port 5000 and responds with the result
     * of the mathematical expression sent by the client in the packet.
     */
    class UDPServer
    /**
     * Initializes a new UDP server with a DatagramSocket listening on port 5000.
     * Waits for incoming packets containing a mathematical expression and responds
     * with the result of the expression.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    constructor(private var socket: DatagramSocket?) {
        private val `in`: BufferedReader? = null
        private var packet: DatagramPacket? = null
        internal var bufferIn = ByteArray(256)
        internal var bufferOut = ByteArray(256)

        init {
            socket = DatagramSocket(5000)
            println("Server is waiting for UDP packets...")
            while (true) {
                packet = DatagramPacket(bufferIn, bufferIn.size)
                socket!!.receive(packet)

                val packetString = String(packet!!.data, 0, packet!!.length)

                val result = packetString + " = " + compute(packetString)

                println(result)

                bufferOut = result.toByteArray()

                val address = packet!!.address
                val port = packet!!.port
                packet = DatagramPacket(bufferOut, bufferOut.size, address, port)
                socket!!.send(packet)
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
        private fun compute(input: String): String {
            val inputElements =
                input.split("(?<=[-+*/])|(?=[-+*/])".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()

            var result = 0.0

            val firstNum: Double
            val secondNum: Double
            try {
                firstNum = java.lang.Double.parseDouble(inputElements[0])
                secondNum = java.lang.Double.parseDouble(inputElements[2])
                if (inputElements[1] == "+") result = firstNum + secondNum
                if (inputElements[1] == "-") result = firstNum - secondNum
                if (inputElements[1] == "*") result = firstNum * secondNum
                if (inputElements[1] == "/") result = firstNum / secondNum
            } catch (n: NumberFormatException) {
                println(n.message)
            }

            return result.toString()
        }

        companion object {

            /**
             * Starts the UDP server by creating a new instance of UDPServer.
             * @param args the command-line arguments. (IMPORTANT! Edit run configurations to run with the
             * argument 'host name')
             */
            @JvmStatic
            fun main(args: Array<String>) {
                try {
                    UDPServer(null)
                } catch (e: Exception) {
                    println(e.message)
                }

            }
        }
    }
}
