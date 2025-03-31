package types;

public class RedisInteger extends RedisData {
    private final Integer value;

    public RedisInteger(Integer value) {
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        return ":" + value.toString() + "\r\n";
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.INTEGER;
    }

    @Override
    public Integer getRawValue() {
        return value;
    }
}
