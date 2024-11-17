package main.java.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.Socket;

public class ClientHandler {
  public void handleClient(Socket clientSocket) {
    try {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        while(true){
        String line = in.readLine();
        System.out.println(line);
        if(line.equalsIgnoreCase("ping")){
            clientSocket.getOutputStream().write("+PONG\r\n".getBytes());
        }
        }
    } catch (IOException e) {
        System.out.println("lol");
    }

  }
}
