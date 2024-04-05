package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class LazilyParsedNumberTypeAdapter extends TypeAdapter<LazilyParsedNumber> {
  // Normally users should not be able to access and deserialize LazilyParsedNumber because
  // it is an internal type, but implement this nonetheless in case there are legit corner
  // cases where this is possible
  @Override
  public LazilyParsedNumber read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return new LazilyParsedNumber(in.nextString());
  }

  @Override
  public void write(JsonWriter out, LazilyParsedNumber value) throws IOException {
    out.value(value);
  }
}
