package types;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class RedisSimpleStringTest {

    @Test
    public void simple_string_data_type_equals_simplestring() {
        RedisSimpleString simpleString = new RedisSimpleString("abcd");
        assertThat(simpleString.getType()).isEqualTo(RedisDataType.SIMPLE_STRING);
    }

    @Test 
    public void simple_string_get_formatted_value_is_correct() {
        RedisSimpleString simpleString = new RedisSimpleString("abcd");
        assertThat(simpleString.getFormattedValue()).isEqualTo("+abcd\r\n");
    }

}
