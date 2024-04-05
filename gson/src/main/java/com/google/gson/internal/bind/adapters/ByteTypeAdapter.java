package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ByteTypeAdapter extends TypeAdapter<Number> {
  private static final int MAX_UNSIGNED_CHAR_VALUE = 255;

  @Override
  public Number read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    int intValue;
    try {
      intValue = in.nextInt();
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(e);
    }
    if (intValue > MAX_UNSIGNED_CHAR_VALUE || intValue < Byte.MIN_VALUE) {
      throw new JsonSyntaxException(
          "Lossy conversion from " + intValue + " to byte; at path " + in.getPreviousPath());
    }
    return (byte) intValue;
  }

  @Override
  public void write(JsonWriter out, Number value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.byteValue());
    }
  }
}
