package types;

public class RedisBoolean extends RedisData {
    private final Boolean value;

    public RedisBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        if (value) sb.append('t');
        else sb.append('f');

        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.BOOLEAN;
    }

    @Override
    public Boolean getRawValue() {
        return value;
    }
}
