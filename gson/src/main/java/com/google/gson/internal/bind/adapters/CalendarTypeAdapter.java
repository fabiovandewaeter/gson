package com.google.gson.internal.bind.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarTypeAdapter extends TypeAdapter<Calendar> {
  private static final String YEAR = "year";
  private static final String MONTH = "month";
  private static final String DAY_OF_MONTH = "dayOfMonth";
  private static final String HOUR_OF_DAY = "hourOfDay";
  private static final String MINUTE = "minute";
  private static final String SECOND = "second";

  @Override
  public Calendar read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    in.beginObject();
    int year = 0;
    int month = 0;
    int dayOfMonth = 0;
    int hourOfDay = 0;
    int minute = 0;
    int second = 0;
    while (in.peek() != JsonToken.END_OBJECT) {
      String name = in.nextName();
      int value = in.nextInt();
      switch (name) {
        case YEAR:
          year = value;
          break;
        case MONTH:
          month = value;
          break;
        case DAY_OF_MONTH:
          dayOfMonth = value;
          break;
        case HOUR_OF_DAY:
          hourOfDay = value;
          break;
        case MINUTE:
          minute = value;
          break;
        case SECOND:
          second = value;
          break;
        default:
          // Ignore unknown JSON property
      }
    }
    in.endObject();
    return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
  }

  @Override
  public void write(JsonWriter out, Calendar value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.beginObject();
    out.name(YEAR);
    out.value(value.get(Calendar.YEAR));
    out.name(MONTH);
    out.value(value.get(Calendar.MONTH));
    out.name(DAY_OF_MONTH);
    out.value(value.get(Calendar.DAY_OF_MONTH));
    out.name(HOUR_OF_DAY);
    out.value(value.get(Calendar.HOUR_OF_DAY));
    out.name(MINUTE);
    out.value(value.get(Calendar.MINUTE));
    out.name(SECOND);
    out.value(value.get(Calendar.SECOND));
    out.endObject();
  }
}
