import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 6379;
    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      while (true) {
        // loop forever and listen for clients
        clientSocket = serverSocket.accept();
        final Socket socket = clientSocket;
        System.out.println("Successful connection");
        Thread clientThread = new Thread(() -> handleClient(socket));
        // Start the thread. The main loop continues immediately.
        clientThread.start();
        System.out.println("Client started");
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }

  private static void handleClient(Socket clientSocket) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      OutputStream outputStream = clientSocket.getOutputStream();
      while (true) {
        String line = in.readLine();
        System.out.println("Command: " + line);
        outputStream.write("+PONG\r\n".getBytes());
      }
    } catch (IOException e) {
        // Catch IO errors during communication or closing for this specific client
        System.err.println("IOException" + ": " + e.getMessage());
    } finally {
        // Socket and streams are guaranteed closed by try-with-resources, even if exceptions occurred.
        System.out.println("Finished handling client");
    }
  }
}


