package socketexamples;

/**
 *
 * @author Ian Gorton
 * Basic socket server that implements a thread-per-connection model:
 * 1) starts and listens for connections on port 12031
 * 2) When a connection received, spawn a thread to handle connection
 * 
 */



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;


public class UDPServer {

  public static void main(String[] args) throws Exception {
    byte[] buf = new byte[256];
    // create socket listener
    DatagramSocket socket = new DatagramSocket(12031);

    // create object o count active threads
    ActiveCount threadCount = new ActiveCount();
    System.out.println("Server started .....");
    while (true) {
      // acept connection and start thread
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

      UDPHandlerThread server = new UDPHandlerThread (socket, packet, threadCount);
      server.start();
    }
  }
}

