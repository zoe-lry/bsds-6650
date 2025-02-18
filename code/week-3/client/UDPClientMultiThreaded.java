package socketexamples;


/**
 * Simple client to send request via a socket.
 * Accepts host and port via command lina, defauklts to localhost and port 12031
 * @author Ian Gorton
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class UDPClientMultiThreaded {
    private static final int BUFFER_SIZE = 1024;
    static CyclicBarrier barrier;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        String hostName;
        final int MAX_THREADS = 100;
        int port;
        
        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= "localhost";
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
            new UDPClientThread(hostName, port, barrier).start();
        });
        // TO DO wait for all threads to comple
        barrier.await(); // Main thread also waits here

        System.out.println("Terminating ....");
        System.out.println("Wall Time: " + (int)(System.currentTimeMillis() -  start));

    }
}
