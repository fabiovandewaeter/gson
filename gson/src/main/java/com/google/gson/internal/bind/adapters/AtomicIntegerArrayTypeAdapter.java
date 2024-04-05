package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayTypeAdapter extends TypeAdapter<AtomicIntegerArray> {
  @Override
  public AtomicIntegerArray read(JsonReader in) throws IOException {
    List<Integer> list = new ArrayList<>();
    in.beginArray();
    while (in.hasNext()) {
      try {
        int integer = in.nextInt();
        list.add(integer);
      } catch (NumberFormatException e) {
        throw new JsonSyntaxException(e);
      }
    }
    in.endArray();
    int length = list.size();
    AtomicIntegerArray array = new AtomicIntegerArray(length);
    for (int i = 0; i < length; ++i) {
      array.set(i, list.get(i));
    }
    return array;
  }

  @Override
  public void write(JsonWriter out, AtomicIntegerArray value) throws IOException {
    out.beginArray();
    for (int i = 0, length = value.length(); i < length; i++) {
      out.value(value.get(i));
    }
    out.endArray();
  }
}
