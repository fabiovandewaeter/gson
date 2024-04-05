package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.net.URL;

public class URLTypeAdapter extends TypeAdapter<URL> {
  @Override
  public URL read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    String nextString = in.nextString();
    return nextString.equals("null") ? null : new URL(nextString);
  }

  @Override
  public void write(JsonWriter out, URL value) throws IOException {
    out.value(value == null ? null : value.toExternalForm());
  }
}
