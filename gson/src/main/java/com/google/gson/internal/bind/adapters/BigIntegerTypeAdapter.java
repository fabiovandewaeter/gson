package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.NumberLimits;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerTypeAdapter extends TypeAdapter<BigInteger> {
  @Override
  public BigInteger read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    String s = in.nextString();
    try {
      return NumberLimits.parseBigInteger(s);
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(
          "Failed parsing '" + s + "' as BigInteger; at path " + in.getPreviousPath(), e);
    }
  }

  @Override
  public void write(JsonWriter out, BigInteger value) throws IOException {
    out.value(value);
  }
}
