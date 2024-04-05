package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.TroubleshootingGuide;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

@SuppressWarnings("rawtypes")
public class ClassTypeAdapter extends TypeAdapter<Class> {

  @Override
  public void write(JsonWriter out, Class value) throws IOException {
    throw new UnsupportedOperationException(
        "Attempted to serialize java.lang.Class: "
            + value.getName()
            + ". Forgot to register a type adapter?"
            + "\nSee "
            + TroubleshootingGuide.createUrl("java-lang-class-unsupported"));
  }

  @Override
  public Class read(JsonReader in) throws IOException {
    throw new UnsupportedOperationException(
        "Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?"
            + "\nSee "
            + TroubleshootingGuide.createUrl("java-lang-class-unsupported"));
  }
}
