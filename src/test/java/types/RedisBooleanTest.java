package types;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import org.junit.Test;

public class RedisBooleanTest {
    @Test
    public void boolean_data_type_is_redis_boolean() {
        RedisBoolean bool = new RedisBoolean(true);
        assertThat(bool.getType()).isEqualTo(RedisDataType.BOOLEAN);
    }

    @Test 
    public void boolean_formatted_value_is_correct() {
        RedisBoolean bool = new RedisBoolean(true);
        assertThat(bool.getFormattedValue()).isEqualTo("#t\r\n");
    }

    @Test
    public void integer_raw_value_is_integer() {
        RedisBoolean bool = new RedisBoolean(true);
        assertThat(bool.getRawValue()).isEqualTo(true);
    }

}
