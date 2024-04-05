package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class FloatAdapterType extends TypeAdapter<Number> {
  @Override
  public Number read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return (float) in.nextDouble();
  }

  @Override
  public void write(JsonWriter out, Number value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      // For backward compatibility don't call `JsonWriter.value(float)` because that method
      // has been newly added and not all custom JsonWriter implementations might override
      // it yet
      Number floatNumber = value instanceof Float ? value : value.floatValue();
      out.value(floatNumber);
    }
  }
}
