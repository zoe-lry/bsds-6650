package socketexamples;

/**
 * @author Ian Gorton Basic socket server that implements a thread-per-connection model: 1) starts
 * and listens for connections on port 12031 2) When a connection received, spawn a thread to handle
 * connection
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPHandlerThread extends Thread {

  private DatagramSocket serverSocket;
  private final DatagramPacket receivePacket;
  private final ActiveCount threadCount;
  private String serverName;

  private byte[] buf = new byte[256];

  public UDPHandlerThread(DatagramSocket serverSocket,
      DatagramPacket receivePacket,
      ActiveCount threadCount) {
    this.serverSocket = serverSocket;
    this.receivePacket = receivePacket;
    this.threadCount = threadCount;
  }


  public void run() {
    threadCount.incrementCount();

    try {
      String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
      InetAddress clientAddress = receivePacket.getAddress();
      int clientPort = receivePacket.getPort();
      System.out.println("Received from " + clientAddress.getHostName() + ":" + clientPort +
          " - " + clientMessage);

      // Build a response
      String responseMessage = "Active Server Thread Count = " + threadCount.getCount();
      byte[] sendData = responseMessage.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress,
          clientPort);
      serverSocket.send(sendPacket);
      System.out.println("Reply sent");

    } catch (Exception e) {
      e.printStackTrace();
    }

    threadCount.decrementCount();
    System.out.println("Thread exiting");
  }
}

