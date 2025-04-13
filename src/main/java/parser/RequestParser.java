package parser;
/*
 * This class will receive input in RESP protocol possibly as a stream: *2\r\n$4\r\nECHO\r\n$3\r\nhey\r\n.
 * It needs to return the command in array form: That's ["ECHO", "hey"]
 * The first element is always the command. Arguments follow up after it.
 * This class should only return the array, don't check if the arguments are enough. That's the executor's job.
 */

import java.io.IOException;
import java.io.InputStream;

import types.RedisArray;
import types.RedisBoolean;
import types.RedisBulkString;
import types.RedisData;
import types.RedisInteger;
import types.RedisSimpleString;

public class RequestParser {
   private final InputStream inputStream; 

   public RequestParser(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public RedisData parse() throws IOException {
      char c = readChar();

      if (c == '*') {
         // array
         return parseArray();
      }
      else if (c == '+') {
         return parseSimpleString();
      }
      else if (c == '$') {
         // bulk string
         return parseBulkString();
      }
      else if (c == '#') {
         // boolean
         return parseBoolean();
      } 
      else if (c == ':') {
         return parseInteger();
      }
      return null;
   }

   private RedisData parseInteger() throws IOException {
      return new RedisInteger(Integer.valueOf((int)readInteger()));
   }

   private RedisData parseArray() throws IOException {
      int length = (int) readInteger();
      RedisData[] values = new RedisData[length];
      for (int i = 0; i < length; i++) {
         RedisData element = parse();
         values[i] = element;
      }
      return new RedisArray(values);
   }

   private RedisData parseBoolean() throws IOException {
      return new RedisBoolean(readString().equals("t"));
   }

   private RedisData parseSimpleString() throws IOException {
      return new RedisSimpleString(readString());
   }

   private RedisData parseBulkString() throws IOException {
     long length = readInteger();

     if (length < 0) {
         skipNBytes(2);
         return new RedisBulkString(null);
     }
     return new RedisBulkString(readString());
   }

   private long readInteger() throws IOException {
        long value = 0;
        boolean isNegative = false;
        char c = readChar();
        while (c != '\r') {
            if (c == '-') isNegative = true;
            else value = value * 10 + c - '0';
            c = readChar();
        }
        skipNBytes(1); // skip the '\n' last character
        if (isNegative) return -value;
        else return value;
    }

   private char readChar() throws IOException {
      int c = -1;
      while (c == -1) c = inputStream.read(); // block until client sends data
      return (char) c;
   }
   
   private String readString() throws IOException {
      StringBuilder sb = new StringBuilder();
      while (true) {
         char cur = readChar();
         if (cur == '\r') {
            break;
         }

         sb.append(cur);
      }
      skipNBytes(1); // skip the '\n' last character
      return sb.toString();
   }

   private void skipNBytes(int n) throws IOException {
      while (n > 0) {
         readChar();
         n--;
      }
   }
}
