package com.google.gson.internal.bind.adapters;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.BitSet;

public class BitSetTypeAdapter extends TypeAdapter<BitSet> {
  private static final int TRUE = 1;
  private static final int FALSE = 0;

  @Override
  public BitSet read(JsonReader in) throws IOException {
    BitSet bitset = new BitSet();
    in.beginArray();
    int i = 0;
    JsonToken tokenType = in.peek();
    while (tokenType != JsonToken.END_ARRAY) {
      boolean set;
      switch (tokenType) {
        case NUMBER:
        case STRING:
          int intValue = in.nextInt();
          if (intValue == 0) {
            set = false;
          } else if (intValue == 1) {
            set = true;
          } else {
            throw new JsonSyntaxException(
                "Invalid bitset value "
                    + intValue
                    + ", expected 0 or 1; at path "
                    + in.getPreviousPath());
          }
          break;
        case BOOLEAN:
          set = in.nextBoolean();
          break;
        default:
          throw new JsonSyntaxException(
              "Invalid bitset value type: " + tokenType + "; at path " + in.getPath());
      }
      if (set) {
        bitset.set(i);
      }
      ++i;
      tokenType = in.peek();
    }
    in.endArray();
    return bitset;
  }

  @Override
  public void write(JsonWriter out, BitSet src) throws IOException {
    out.beginArray();
    for (int i = 0, length = src.length(); i < length; i++) {
      int value = src.get(i) ? TRUE : FALSE;
      out.value(value);
    }
    out.endArray();
  }
}
