package types;

public class RedisSimpleString extends RedisData {
    private final String value;

    public RedisSimpleString(String value) {
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(value);
        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.SIMPLE_STRING;
    }

    @Override
    public String getRawValue() {
        return value;
    }    
}
