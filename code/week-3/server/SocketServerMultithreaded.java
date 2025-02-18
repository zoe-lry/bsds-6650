package socketexamples;

/**
 *
 * @author Ian Gorton
 * Basic socket server that implements a thread-per-connection model:
 * 1) starts and listens for connections on port 12031
 * 2) When a connection received, spawn a thread to handle connection
 * 
 */



import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketServerMultithreaded {
  public static void main(String[] args) throws Exception {
    int POOL_SIZE = 1;
    ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
    // create socket listener

    ServerSocket m_ServerSocket = new ServerSocket(12031);
    // create object o count active threads
    ActiveCount threadCount = new ActiveCount();
    System.out.println("Server started .....");
    while (true) {
      // acept connection and start thread  
      Socket clientSocket = m_ServerSocket.accept();
//      System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostName());
      executorService.submit(new SocketHandlerThread (clientSocket, threadCount));
    }
  }
}

