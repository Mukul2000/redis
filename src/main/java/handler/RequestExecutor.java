package handler;

import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import types.RedisArray;
import types.RedisBoolean;
import types.RedisBulkString;
import types.RedisData;
import types.RedisDataType;
import types.RedisError;
import types.RedisSimpleString;

public class RequestExecutor {

    public static RedisData execute(RedisData request) {
        String command = getString(request);
        if (command == null) {
            return new RedisError("ERR unknown command");
        }
        switch (command.toUpperCase()) {
            case "PING":
                return handlePingCommand(request);
            case "SET":
                return handleSetCommand(request);
            case "GET":
                return handleGetCommand(request);
            case "DEL":
                return handleDelCommand(request);
            case "ECHO":
                return handleEchoCommand(request);
            default:
                return new RedisError("unknown command");
        }
    }

    private static RedisData handlePingCommand(RedisData request) {
        System.out.println("PING command received" + request.getFormattedValue());
        return new RedisBulkString("PONG");
    }

    private static RedisData handleGetCommand(RedisData request) {
        return new RedisBoolean(true);
    }

    private static RedisData handleSetCommand(RedisData request) {
        return new RedisBoolean(true);
    }

    private static RedisData handleDelCommand(RedisData request) {
        return new RedisBoolean(true);
    }

    private static RedisData handleEchoCommand(RedisData request) {
        if (request.getType() == RedisDataType.ARRAY && ((RedisArray) request).getRawValue().length == 2) {
            return ((RedisArray) request).getRawValue()[1];
        }
        return new RedisError("ERR invalid request format for ECHO command");
    }

    private static String getString(RedisData data) {
        if (data == null)
            return null;
        if (data.getType() == RedisDataType.ARRAY) {
            RedisArray array = (RedisArray) data;
            RedisData[] values = array.getRawValue();
            if (values.length == 0)
                return null;
            if (values[0].getType() == RedisDataType.BULK_STRING) {
                return ((RedisBulkString) values[0]).getRawValue();
            } else if (values[0].getType() == RedisDataType.SIMPLE_STRING) {
                return ((RedisSimpleString) values[0]).getRawValue();
            }
        } else if (data.getType() == RedisDataType.BULK_STRING) {
            return ((RedisBulkString) data).getRawValue();
        } else if (data.getType() == RedisDataType.SIMPLE_STRING) {
            return ((RedisSimpleString) data).getRawValue();
        }
        return null;
    }
}
