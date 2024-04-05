package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> {
  @Override
  public Boolean read(JsonReader in) throws IOException {
    JsonToken peek = in.peek();
    if (peek == JsonToken.NULL) {
      in.nextNull();
      return null;
    } else if (peek == JsonToken.STRING) {
      // support strings for compatibility with GSON 1.7
      return Boolean.parseBoolean(in.nextString());
    }
    return in.nextBoolean();
  }

  @Override
  public void write(JsonWriter out, Boolean value) throws IOException {
    out.value(value);
  }
}
