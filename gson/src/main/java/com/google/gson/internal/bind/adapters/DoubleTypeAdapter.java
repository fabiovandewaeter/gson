package com.google.gson.internal.bind.adapters;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DoubleTypeAdapter extends TypeAdapter<Number> {
    @Override
        public Number read(JsonReader in) throws IOException {
          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }
          return in.nextDouble();
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
          if (value == null) {
            out.nullValue();
          } else {
            out.value(value.doubleValue());
          }
        }
}
