package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ShortTypeAdapter extends TypeAdapter<Number> {
  private static final int MAX_SHORT_VALUE = 65535;

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
    if (intValue > MAX_SHORT_VALUE || intValue < Short.MIN_VALUE) {
      throw new JsonSyntaxException(
          "Lossy conversion from " + intValue + " to short; at path " + in.getPreviousPath());
    }
    return (short) intValue;
  }

  @Override
  public void write(JsonWriter out, Number value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.shortValue());
    }
  }
}
