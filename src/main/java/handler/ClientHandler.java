package handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import parser.RequestParser;
import types.RedisData;

public class ClientHandler implements Runnable {
  private Socket clientSocket;
  private InputStream inputStream;
  private OutputStream outputStream;

  public ClientHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    handleClient();
  }

  private void handleClient() {
    try {
      inputStream = clientSocket.getInputStream();
      outputStream = clientSocket.getOutputStream();
      RequestParser requestParser = new RequestParser(inputStream);
      while (true) {
        RedisData request = requestParser.parse();
        System.out.println("Request: " + request);
        if (request == null) continue;
        RedisData response = RequestExecutor.execute(request);
        System.out.println("Response: " + response);
        outputStream.write(response.getFormattedValue().getBytes());
        outputStream.flush();
      }
    } catch (IOException e) {
      System.out.println("lol");
    } finally {
      try {
        if (clientSocket != null)
          clientSocket.close();
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
}
