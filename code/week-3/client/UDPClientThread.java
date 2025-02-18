package socketexamples;

/**
 * Simple skeleton socket client thread that coordinates termination
 * with a cyclic barrier to demonstration barrier synchronization
 * @author Ian Gorton
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// Sockets of this class are coordinated  by a CyclicBarrier which pauses all threads 
// until the last one completes. At this stage, all threads terminate

public class UDPClientThread extends Thread {

    private long clientID;
    String hostName;
    int port;
    CyclicBarrier synk;
    private static final int BUFFER_SIZE = 1024;

    public UDPClientThread(String hostName, int port, CyclicBarrier barrier) {
        this.hostName = hostName;
        this.port = port;
        this.clientID = this.threadId();
        this.synk = barrier;

    }

    public void run() {

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            String message = "Hello from UDP client";
            byte[] sendBuffer = message.getBytes();

            InetAddress serverAddress = InetAddress.getByName(hostName);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                serverAddress, port);
            System.out.println("Sending message: " + message);
            clientSocket.send(sendPacket);
            // Prepare a packet to receive response
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, BUFFER_SIZE);

            // Block until a response is received
            clientSocket.receive(receivePacket);

            // Convert the response back into a String
            String serverResponse = new String(receivePacket.getData(), 0,
                receivePacket.getLength());
            System.out.println("Received from server: " + serverResponse);


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }

    // After sending messages, log crossing the barrier
      try {
        this.synk.await();
          System.out.println(this.threadId() + " has crossed the barrier and continues execution");
      } catch (InterruptedException | BrokenBarrierException e) {
        throw new RuntimeException(e);
      }

    }
}
