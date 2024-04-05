package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;

public class LocaleTypeAdapter extends TypeAdapter<Locale> {
  @Override
  public Locale read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    String locale = in.nextString();
    StringTokenizer tokenizer = new StringTokenizer(locale, "_");
    String language = null;
    String country = null;
    String variant = null;
    if (tokenizer.hasMoreElements()) {
      language = tokenizer.nextToken();
    }
    if (tokenizer.hasMoreElements()) {
      country = tokenizer.nextToken();
    }
    if (tokenizer.hasMoreElements()) {
      variant = tokenizer.nextToken();
    }
    if (country == null && variant == null) {
      return new Locale(language);
    } else if (variant == null) {
      return new Locale(language, country);
    } else {
      return new Locale(language, country, variant);
    }
  }

  @Override
  public void write(JsonWriter out, Locale value) throws IOException {
    out.value(value == null ? null : value.toString());
  }
}
