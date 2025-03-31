package types;

import static org.junit.Assert.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import org.junit.Test;

public class RedisIntegerTest {
    
    @Test
    public void integer_data_type_is_redis_integer() {
        RedisInteger integer = new RedisInteger(1);
        assertThat(integer.getType()).isEqualTo(RedisDataType.INTEGER);
    }

    @Test 
    public void integer_formatted_value_is_correct() {
        RedisInteger integer = new RedisInteger(1234);
        assertThat(integer.getFormattedValue()).isEqualTo(":1234\r\n");
    }

    @Test
    public void integer_raw_value_is_integer() {
        RedisInteger integer = new RedisInteger(1234);
        assertThat(integer.getRawValue()).isEqualTo(1234);
    }

}
