package handler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

public class ClientHandlerTest {
    @Test
    public void send_request_then_terminate_conn() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("*2\r\n$4\r\nECHO\r\n$5\r\nhello\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        ClientHandler clientHandler = new ClientHandler(socket);

        clientHandler.run();

        inputStream.close();
        assertThat(socket.isClosed()).isTrue();
        String output = outputStream.toString();
        System.out.println("output: " + output);
    }
}
