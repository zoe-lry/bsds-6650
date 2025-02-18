package socketexamples;


/**
 * Simple client to send request via a socket.
 * Accepts host and port via command lina, defauklts to localhost and port 12031
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

public class UDPClientSingleThreaded {
    private static final int BUFFER_SIZE = 1024;


    public static void main(String[] args)  {
        String hostName;
        int port;
        
        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= "localhost";
            port = 12031;  // default port in SocketServer
        }
        long clientID = Thread.currentThread().getId();
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            String message =  "Hello from UDP client";
            byte[] sendBuffer = message.getBytes();

            InetAddress serverAddress = InetAddress.getByName(hostName);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);
            System.out.println("Sending message: " + message);
            clientSocket.send(sendPacket);
            // Prepare a packet to receive response
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, BUFFER_SIZE);

            // Block until a response is received
            clientSocket.receive(receivePacket);

            // Convert the response back into a String
            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received from server: " + serverResponse);



        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
