package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTypeAdapter extends TypeAdapter<AtomicInteger> {
  @Override
  public AtomicInteger read(JsonReader in) throws IOException {
    try {
      return new AtomicInteger(in.nextInt());
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(e);
    }
  }

  @Override
  public void write(JsonWriter out, AtomicInteger value) throws IOException {
    out.value(value.get());
  }
}
