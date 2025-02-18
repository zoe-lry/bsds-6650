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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// Sockets of this class are coordinated  by a CyclicBarrier which pauses all threads 
// until the last one completes. At this stage, all threads terminate

public class SocketClientThread extends Thread {
    private long clientID;
    String hostName;
    int port;
    CyclicBarrier synk;
    
    public SocketClientThread(String hostName, int port, CyclicBarrier barrier) {
        this.hostName = hostName;
        this.port = port;
        this.clientID = this.threadId();
        this.synk = barrier;
        
    }
    
    public void run() {
        
        try {
            // TO DO insert code to pass 1k messages to the SocketServer
//            Thread.sleep(1000);
            Socket s = new Socket(hostName, port);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            System.out.println("Client Name is " + this.clientID);
            out.println("Client Name is " + this.clientID);
            System.out.println(in.readLine());

            // After sending messages, log crossing the barrier
            this.synk.await();
            System.out.println(this.threadId() + " has crossed the barrier and continues execution");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } catch (BrokenBarrierException | InterruptedException e) {
          throw new RuntimeException(e);
        }

      // TO DO insert code to wait on the CyclicBarrier


        
    }
}
