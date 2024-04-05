package com.google.gson.internal.bind.adapters;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class CharacterTypeAdapter extends TypeAdapter<Character> {
    @Override
        public Character read(JsonReader in) throws IOException {
          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          }
          String str = in.nextString();
          if (str.length() != 1) {
            throw new JsonSyntaxException(
                "Expecting character, got: " + str + "; at " + in.getPreviousPath());
          }
          return str.charAt(0);
        }

        @Override
        public void write(JsonWriter out, Character value) throws IOException {
          out.value(value == null ? null : String.valueOf(value));
        }
}
