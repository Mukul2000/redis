package parser;

import org.junit.Test;

import types.RedisArray;
import types.RedisBoolean;
import types.RedisBulkString;
import types.RedisData;
import types.RedisDataType;
import types.RedisInteger;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.mockito.Mockito.*;

public class RequestParserTest {
    @Test
    public void parse_simple_string_success() throws IOException {
        String command = "+OKLOL\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.SIMPLE_STRING);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo("OKLOL");
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_empty_simple_string_success() throws IOException {
        String command = "+\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.SIMPLE_STRING);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo("");
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_data_type_stream_throws_exception_expectIOException() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);

        when(mockInputStream.read()).thenThrow(new IOException("Simulated IO error"));
        when(mockInputStream.read(any(byte[].class))).thenThrow(new IOException("Simulated IO error"));
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException("Simulated IO error"));

        RequestParser parser = new RequestParser(mockInputStream);

        assertThrows(IOException.class, () -> parser.parse());
    }

    @Test
    public void parse_bulk_string_success() throws IOException {
        String command = "$4\r\ntest\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.BULK_STRING);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo("test");
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_empty_bulk_string_success() throws IOException {
        String command = "$0\r\n\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.BULK_STRING);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo("");
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_bulk_string_with_spaces_success() throws IOException {
        String command = "$11\r\nHello World\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.BULK_STRING);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo("Hello World");
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_integer_success() throws IOException {
        String command = ":432\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.INTEGER);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo(432);
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_negative_integer_success() throws IOException {
        String command = ":-432\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.INTEGER);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(parsed.getRawValue()).isEqualTo(-432);
        assertThat(in.available()).isEqualTo(0);
    }

    @Test
    public void parse_simple_array_success()  throws IOException {
        String command = "*3\r\n#t\r\n$5\r\nhello\r\n:1234\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.ARRAY);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(in.available()).isEqualTo(0);       
    }

    @Test
    public void parse_nested_array_success()  throws IOException {
        String command = "*1\r\n*2\r\n:123\r\n$4\r\ntest\r\n";
        InputStream in = new ByteArrayInputStream(command.getBytes());
        RequestParser parser = new RequestParser(in);

        RedisData parsed = parser.parse();

        assertThat(parsed.getType()).isEqualTo(RedisDataType.ARRAY);
        assertThat(parsed.getFormattedValue()).isEqualTo(command);
        assertThat(in.available()).isEqualTo(0);       
    }
}
