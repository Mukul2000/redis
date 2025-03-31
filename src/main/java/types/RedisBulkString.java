package types;

public class RedisBulkString extends RedisData {
    private final String value;

    public RedisBulkString(String value) {
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        return "$" + ((Integer)value.length()).toString() + "\r\n" + value + "\r\n";
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.BULK_STRING;
    }

    @Override
    public String getRawValue() {
        return value;
    }    
}
