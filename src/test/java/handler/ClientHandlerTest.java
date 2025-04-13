package handler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import org.junit.Test;

import types.RedisBulkString;

public class ClientHandlerTest {
    @Test
    public void send_command_success() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("*2\r\n$4\r\nECHO\r\n$5\r\nhello\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        ClientHandler clientHandler = new ClientHandler(socket);

        Thread clientThread = new Thread(clientHandler);
        clientThread.start();

        Thread.sleep(1000);
        String response = outputStream.toString();
        assertThat(response).isEqualTo("$5\r\nhello\r\n");

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void send_successive_commands_success() throws Exception {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStreamToInput = new PipedOutputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStreamToInput.write("*2\r\n$4\r\nECHO\r\n$5\r\nhello\r\n".getBytes());

        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        ClientHandler clientHandler = new ClientHandler(socket);

        Thread clientThread = new Thread(clientHandler);
        clientThread.start();

        Thread.sleep(1000);
        String response = outputStream.toString();
        assertThat(response).isEqualTo("$5\r\nhello\r\n");
        outputStream.reset();

        outputStreamToInput.write("*1\r\n$4\r\nPING\r\n".getBytes());
        Thread.sleep(1000);
        response = outputStream.toString();
        assertThat(response).isEqualTo((new RedisBulkString("PONG")).getFormattedValue());

        inputStream.close();
        outputStream.close();
        outputStreamToInput.close();
    }

    @Test
    public void input_stream_throws_IOException_socket_closed() throws Exception {
        final boolean[] isSocketClosed = {false};
        ByteArrayInputStream inputStream = mock(ByteArrayInputStream.class);
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(socket.isClosed()).thenReturn(false);
        // doAnswer is for void methods
        doAnswer(invocation -> {
            isSocketClosed[0] = true;
            return null;
        }).when(socket).close();
        when(socket.isClosed()).thenAnswer(invocation -> isSocketClosed[0]);
        when(inputStream.read()).thenThrow(new IOException("something wrong bro"));

        ClientHandler clientHandler = new ClientHandler(socket);
        Thread clientThread = new Thread(clientHandler);
        clientThread.start();

        Thread.sleep(1000);
        System.out.println(socket.isClosed());
        assertThat(socket.isClosed()).isTrue();
    }
}
