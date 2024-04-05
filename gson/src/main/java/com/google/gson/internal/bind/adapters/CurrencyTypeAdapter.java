package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Currency;

public class CurrencyTypeAdapter extends TypeAdapter<Currency> {
  @Override
  public Currency read(JsonReader in) throws IOException {
    String s = in.nextString();
    try {
      return Currency.getInstance(s);
    } catch (IllegalArgumentException e) {
      throw new JsonSyntaxException(
          "Failed parsing '" + s + "' as Currency; at path " + in.getPreviousPath(), e);
    }
  }

  @Override
  public void write(JsonWriter out, Currency value) throws IOException {
    out.value(value.getCurrencyCode());
  }
}
