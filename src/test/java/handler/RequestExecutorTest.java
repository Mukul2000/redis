package handler;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import handler.RequestExecutor;
import types.RedisArray;
import types.RedisBulkString;
import types.RedisData;
import types.RedisError;

public class RequestExecutorTest {
    
    @Test
    public void ping_success() {
        RedisData request = new RedisArray(new RedisData[] {new RedisBulkString("PING")});
        RedisData expectedResponse = new RedisBulkString("PONG");

        RedisData response = RequestExecutor.execute(request);
        assertThat(response.getFormattedValue()).isEqualTo(expectedResponse.getFormattedValue());
    }

    @Test
    public void echo_success() {
        RedisData request = new RedisArray(new RedisData[] {new RedisBulkString("ECHO"), new RedisBulkString("Hello World!")});
        RedisData expectedResponse = new RedisBulkString("Hello World!");

        RedisData response = RequestExecutor.execute(request);
        assertThat(response.getFormattedValue()).isEqualTo(expectedResponse.getFormattedValue());
    }

    @Test
    public void echo_malformed_command_throws_RedisError() {
        RedisData request = new RedisArray(new RedisData[] {new RedisBulkString("ECHO")});
        RedisData expectedResponse = new RedisError("ERR invalid request format for ECHO command");

        RedisData response = RequestExecutor.execute(request);

        assertThat(response.getType()).isEqualTo(expectedResponse.getType());
        assertThat(response.getFormattedValue()).isEqualTo(expectedResponse.getFormattedValue());
    }
}
