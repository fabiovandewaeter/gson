package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class JsonElementTypeAdapter extends TypeAdapter<JsonElement> {
  /**
   * Tries to begin reading a JSON array or JSON object, returning {@code null} if the next element
   * is neither of those.
   */
  private JsonElement tryBeginNesting(JsonReader in, JsonToken peeked) throws IOException {
    switch (peeked) {
      case BEGIN_ARRAY:
        in.beginArray();
        return new JsonArray();
      case BEGIN_OBJECT:
        in.beginObject();
        return new JsonObject();
      default:
        return null;
    }
  }

  /** Reads a {@link JsonElement} which cannot have any nested elements */
  private JsonElement readTerminal(JsonReader in, JsonToken peeked) throws IOException {
    switch (peeked) {
      case STRING:
        return new JsonPrimitive(in.nextString());
      case NUMBER:
        String number = in.nextString();
        return new JsonPrimitive(new LazilyParsedNumber(number));
      case BOOLEAN:
        return new JsonPrimitive(in.nextBoolean());
      case NULL:
        in.nextNull();
        return JsonNull.INSTANCE;
      default:
        // When read(JsonReader) is called with JsonReader in invalid state
        throw new IllegalStateException("Unexpected token: " + peeked);
    }
  }

  @Override
  public JsonElement read(JsonReader in) throws IOException {
    if (in instanceof JsonTreeReader) {
      return ((JsonTreeReader) in).nextJsonElement();
    }

    // Either JsonArray or JsonObject
    JsonElement current;
    JsonToken peeked = in.peek();

    current = tryBeginNesting(in, peeked);
    if (current == null) {
      return readTerminal(in, peeked);
    }

    Deque<JsonElement> stack = new ArrayDeque<>();

    while (true) {
      while (in.hasNext()) {
        String name = null;
        // Name is only used for JSON object members
        if (current instanceof JsonObject) {
          name = in.nextName();
        }

        peeked = in.peek();
        JsonElement value = tryBeginNesting(in, peeked);
        boolean isNesting = value != null;

        if (value == null) {
          value = readTerminal(in, peeked);
        }

        if (current instanceof JsonArray) {
          ((JsonArray) current).add(value);
        } else {
          ((JsonObject) current).add(name, value);
        }

        if (isNesting) {
          stack.addLast(current);
          current = value;
        }
      }

      // End current element
      if (current instanceof JsonArray) {
        in.endArray();
      } else {
        in.endObject();
      }

      if (stack.isEmpty()) {
        return current;
      } else {
        // Continue with enclosing element
        current = stack.removeLast();
      }
    }
  }

  @Override
  public void write(JsonWriter out, JsonElement value) throws IOException {
    if (value == null || value.isJsonNull()) {
      out.nullValue();
    } else if (value.isJsonPrimitive()) {
      JsonPrimitive primitive = value.getAsJsonPrimitive();
      if (primitive.isNumber()) {
        out.value(primitive.getAsNumber());
      } else if (primitive.isBoolean()) {
        out.value(primitive.getAsBoolean());
      } else {
        out.value(primitive.getAsString());
      }

    } else if (value.isJsonArray()) {
      out.beginArray();
      for (JsonElement e : value.getAsJsonArray()) {
        write(out, e);
      }
      out.endArray();

    } else if (value.isJsonObject()) {
      out.beginObject();
      for (Map.Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
        out.name(e.getKey());
        write(out, e.getValue());
      }
      out.endObject();

    } else {
      throw new IllegalArgumentException("Couldn't write " + value.getClass());
    }
  }
}
