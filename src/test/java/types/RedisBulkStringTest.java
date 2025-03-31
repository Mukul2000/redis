package types;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class RedisBulkStringTest {

    @Test
    public void bulk_string_correct_data_type() {
        RedisBulkString bulkString = new RedisBulkString("abcd");
        assertThat(bulkString.getType()).isEqualTo(RedisDataType.BULK_STRING);
    }

    @Test 
    public void bulk_string_get_formatted_value_is_correct() {
        RedisBulkString bulkString = new RedisBulkString("abcd");
        assertThat(bulkString.getFormattedValue()).isEqualTo("$4\r\nabcd\r\n");
    }
}
