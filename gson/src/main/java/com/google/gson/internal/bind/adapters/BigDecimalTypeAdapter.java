package com.google.gson.internal.bind.adapters;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.NumberLimits;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {
    @Override
        public BigDecimal read(JsonReader in) throws IOException {
          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }
          String s = in.nextString();
          try {
            return NumberLimits.parseBigDecimal(s);
          } catch (NumberFormatException e) {
            throw new JsonSyntaxException(
                "Failed parsing '" + s + "' as BigDecimal; at path " + in.getPreviousPath(), e);
          }
        }

        @Override
        public void write(JsonWriter out, BigDecimal value) throws IOException {
          out.value(value);
        }
}
