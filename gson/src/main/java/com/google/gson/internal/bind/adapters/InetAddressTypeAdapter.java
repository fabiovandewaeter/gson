package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.net.InetAddress;

public class InetAddressTypeAdapter extends TypeAdapter<InetAddress> {
  @Override
  public InetAddress read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    // regrettably, this should have included both the host name and the host address
    // For compatibility, we use InetAddress.getByName rather than the possibly-better
    // .getAllByName
    @SuppressWarnings("AddressSelection")
    InetAddress addr = InetAddress.getByName(in.nextString());
    return addr;
  }

  @Override
  public void write(JsonWriter out, InetAddress value) throws IOException {
    out.value(value == null ? null : value.getHostAddress());
  }
}
