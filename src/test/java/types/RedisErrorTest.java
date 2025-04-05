package types;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class RedisErrorTest {
    @Test
    public void data_type_is_redis_error() {
        RedisError error = new RedisError("oopsie");
        assertThat(error.getType()).isEqualTo(RedisDataType.ERROR);
    }

    @Test 
    public void error_formatted_value_is_correct() {
        RedisError error = new RedisError("oopsie");
        assertThat(error.getFormattedValue()).isEqualTo("-oopsie\r\n");
    }

    @Test
    public void error_raw_value_is_integer() {
        RedisError error = new RedisError("oopsie");
        assertThat(error.getRawValue()).isEqualTo("oopsie");
    } 
}
