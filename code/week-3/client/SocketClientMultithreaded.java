package socketexamples;

/**
 * Skeleton socket client. 
 * Accepts host/port on command line or defaults to localhost/12031
 * Then (should) starts MAX_Threads and waits for them all to terminate before terminating main()
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SocketClientMultithreaded {
    
    static CyclicBarrier barrier; 
    
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        String hostName;
        final int MAX_THREADS = 100;
        int port;
        
        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= null;
            port = 12031;  // default port in SocketServer
        }
        long start = System.currentTimeMillis();
        /**
         * We need MAX_THREADS + 1 parties:
         *  - MAX_THREADS client threads
         *  - 1 for the main thread
         */
        barrier = new CyclicBarrier(MAX_THREADS + 1);
        // TO DO create and start MAX_THREADS SocketClientThread
        IntStream.range(0, MAX_THREADS).forEach(i -> {
            new SocketClientThread(hostName, port, barrier).start();
        });
        // TO DO wait for all threads to comple
        barrier.await(); // Main thread also waits here
        System.out.println("Terminating ....");
        System.out.println("Wall Time: " + (int)(System.currentTimeMillis() -  start));

                
    }

      
}
