import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import handler.ClientHandler;

public class Main {
  public static void main(String[] args){
    ServerSocket serverSocket = null;
    int port = 6379;
    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
        Socket clientSocket = serverSocket.accept();
        // Create a new thread for the client.
        new Thread(new ClientHandler(clientSocket)).start();
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }

}


