package types;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import org.junit.Test;

public class RedisArrayTest {
        @Test
    public void data_type_is_redis_array() {
        RedisArray arr = new RedisArray(new RedisData[4]);
        assertThat(arr.getType()).isEqualTo(RedisDataType.ARRAY);
    }

    @Test 
    public void formatted_value_is_correct() {
        RedisData[] data = new RedisData[3];
        data[0] = new RedisInteger(124);
        data[1] = new RedisBulkString("hello");
        data[2] = new RedisBoolean(false);
        RedisArray arr = new RedisArray(data);

        assertThat(arr.getFormattedValue()).isEqualTo("*3\r\n:124\r\n$5\r\nhello\r\n#f\r\n");
    }

    @Test
    public void raw_value_is_correct() {
        RedisData[] data = new RedisData[3];
        data[0] = new RedisInteger(124);
        data[1] = new RedisBulkString("hello");
        data[2] = new RedisBoolean(false);

        RedisArray arr = new RedisArray(data);
        assertThat(arr.getRawValue()).isEqualTo(data);
    }
}
