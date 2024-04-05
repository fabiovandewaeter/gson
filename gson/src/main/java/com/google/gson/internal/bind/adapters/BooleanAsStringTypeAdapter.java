package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class BooleanAsStringTypeAdapter extends TypeAdapter<Boolean> {
  @Override
  public Boolean read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return Boolean.valueOf(in.nextString());
  }

  @Override
  public void write(JsonWriter out, Boolean value) throws IOException {
    out.value(value == null ? "null" : value.toString());
  }
}
