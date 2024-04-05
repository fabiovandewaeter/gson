/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.adapters.AtomicBooleanTypeAdapter;
import com.google.gson.internal.bind.adapters.AtomicIntegerArrayTypeAdapter;
import com.google.gson.internal.bind.adapters.AtomicIntegerTypeAdapter;
import com.google.gson.internal.bind.adapters.BigDecimalTypeAdapter;
import com.google.gson.internal.bind.adapters.BigIntegerTypeAdapter;
import com.google.gson.internal.bind.adapters.BitSetTypeAdapter;
import com.google.gson.internal.bind.adapters.BooleanAsStringTypeAdapter;
import com.google.gson.internal.bind.adapters.BooleanTypeAdapter;
import com.google.gson.internal.bind.adapters.ByteTypeAdapter;
import com.google.gson.internal.bind.adapters.CalendarTypeAdapter;
import com.google.gson.internal.bind.adapters.CharacterTypeAdapter;
import com.google.gson.internal.bind.adapters.ClassTypeAdapter;
import com.google.gson.internal.bind.adapters.CurrencyTypeAdapter;
import com.google.gson.internal.bind.adapters.DoubleTypeAdapter;
import com.google.gson.internal.bind.adapters.FloatAdapterType;
import com.google.gson.internal.bind.adapters.InetAddressTypeAdapter;
import com.google.gson.internal.bind.adapters.IntegerTypeAdapter;
import com.google.gson.internal.bind.adapters.JsonElementTypeAdapter;
import com.google.gson.internal.bind.adapters.LazilyParsedNumberTypeAdapter;
import com.google.gson.internal.bind.adapters.LocaleTypeAdapter;
import com.google.gson.internal.bind.adapters.LongTypeAdapter;
import com.google.gson.internal.bind.adapters.ShortTypeAdapter;
import com.google.gson.internal.bind.adapters.StringBufferTypeAdapter;
import com.google.gson.internal.bind.adapters.StringBuilderTypeAdapter;
import com.google.gson.internal.bind.adapters.StringTypeAdapter;
import com.google.gson.internal.bind.adapters.URITypeAdapter;
import com.google.gson.internal.bind.adapters.URLTypeAdapter;
import com.google.gson.internal.bind.adapters.UUIDTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/** Type adapters for basic types. */
public final class TypeAdapters {

  private TypeAdapters() {
    throw new UnsupportedOperationException();
  }

  /**
   * Writes a boolean as a string. Useful for map keys, where booleans aren't otherwise permitted.
   */
  public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new BooleanAsStringTypeAdapter();

  public static final TypeAdapter<Number> LONG = new LongTypeAdapter();
  public static final TypeAdapter<Number> FLOAT = new FloatAdapterType();
  public static final TypeAdapter<Number> DOUBLE = new DoubleTypeAdapter();
  public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new BigDecimalTypeAdapter();
  public static final TypeAdapter<BigInteger> BIG_INTEGER = new BigIntegerTypeAdapter();
  public static final TypeAdapter<LazilyParsedNumber> LAZILY_PARSED_NUMBER =
      new LazilyParsedNumberTypeAdapter();

  public static final TypeAdapterFactory CLASS_FACTORY =
      newFactory(Class.class, new ClassTypeAdapter().nullSafe());
  public static final TypeAdapterFactory BIT_SET_FACTORY =
      newFactory(BitSet.class, new BitSetTypeAdapter().nullSafe());
  public static final TypeAdapterFactory BOOLEAN_FACTORY =
      newFactory(boolean.class, Boolean.class, new BooleanTypeAdapter());
  public static final TypeAdapterFactory BYTE_FACTORY =
      newFactory(byte.class, Byte.class, new ByteTypeAdapter());
  public static final TypeAdapterFactory SHORT_FACTORY =
      newFactory(short.class, Short.class, new ShortTypeAdapter());
  public static final TypeAdapterFactory INTEGER_FACTORY =
      newFactory(int.class, Integer.class, new IntegerTypeAdapter());
  public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY =
      newFactory(AtomicInteger.class, new AtomicIntegerTypeAdapter().nullSafe());
  public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY =
      newFactory(AtomicBoolean.class, new AtomicBooleanTypeAdapter().nullSafe());
  public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY =
      newFactory(AtomicIntegerArray.class, new AtomicIntegerArrayTypeAdapter().nullSafe());
  public static final TypeAdapterFactory CHARACTER_FACTORY =
      newFactory(char.class, Character.class, new CharacterTypeAdapter());
  public static final TypeAdapterFactory STRING_FACTORY =
      newFactory(String.class, new StringTypeAdapter());
  public static final TypeAdapterFactory STRING_BUILDER_FACTORY =
      newFactory(StringBuilder.class, new StringBuilderTypeAdapter());
  public static final TypeAdapterFactory STRING_BUFFER_FACTORY =
      newFactory(StringBuffer.class, new StringBufferTypeAdapter());
  public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, new URLTypeAdapter());
  public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, new URITypeAdapter());
  public static final TypeAdapterFactory INET_ADDRESS_FACTORY =
      newTypeHierarchyFactory(InetAddress.class, new InetAddressTypeAdapter());
  public static final TypeAdapterFactory UUID_FACTORY =
      newFactory(UUID.class, new UUIDTypeAdapter());
  public static final TypeAdapterFactory CURRENCY_FACTORY =
      newFactory(Currency.class, new CurrencyTypeAdapter().nullSafe());
  public static final TypeAdapterFactory CALENDAR_FACTORY =
      newFactoryForMultipleTypes(
          Calendar.class, GregorianCalendar.class, new CalendarTypeAdapter());
  public static final TypeAdapterFactory LOCALE_FACTORY =
      newFactory(Locale.class, new LocaleTypeAdapter());
  public static final TypeAdapter<JsonElement> JSON_ELEMENT = new JsonElementTypeAdapter();
  public static final TypeAdapterFactory JSON_ELEMENT_FACTORY =
      newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);

  private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<String, T> nameToConstant = new HashMap<>();
    private final Map<String, T> stringToConstant = new HashMap<>();
    private final Map<T, String> constantToName = new HashMap<>();

    public EnumTypeAdapter(final Class<T> classOfT) {
      try {
        // Uses reflection to find enum constants to work around name mismatches for obfuscated
        // classes
        // Reflection access might throw SecurityException, therefore run this in privileged
        // context; should be acceptable because this only retrieves enum constants, but does not
        // expose anything else
        Field[] constantFields =
            AccessController.doPrivileged(
                new PrivilegedAction<Field[]>() {
                  @Override
                  public Field[] run() {
                    Field[] fields = classOfT.getDeclaredFields();
                    ArrayList<Field> constantFieldsList = new ArrayList<>(fields.length);
                    for (Field f : fields) {
                      if (f.isEnumConstant()) {
                        constantFieldsList.add(f);
                      }
                    }

                    Field[] constantFields = constantFieldsList.toArray(new Field[0]);
                    AccessibleObject.setAccessible(constantFields, true);
                    return constantFields;
                  }
                });
        for (Field constantField : constantFields) {
          @SuppressWarnings("unchecked")
          T constant = (T) constantField.get(null);
          String name = constant.name();
          String toStringVal = constant.toString();

          SerializedName annotation = constantField.getAnnotation(SerializedName.class);
          if (annotation != null) {
            name = annotation.value();
            for (String alternate : annotation.alternate()) {
              nameToConstant.put(alternate, constant);
            }
          }
          nameToConstant.put(name, constant);
          stringToConstant.put(toStringVal, constant);
          constantToName.put(constant, name);
        }
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
    }

    @Override
    public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String key = in.nextString();
      T constant = nameToConstant.get(key);
      return (constant == null) ? stringToConstant.get(key) : constant;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
      out.value(value == null ? null : constantToName.get(value));
    }
  }

  public static final TypeAdapterFactory ENUM_FACTORY =
      new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          Class<? super T> rawType = typeToken.getRawType();
          if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
            return null;
          }
          if (!rawType.isEnum()) {
            rawType = rawType.getSuperclass(); // handle anonymous subclasses
          }
          @SuppressWarnings({"rawtypes", "unchecked"})
          TypeAdapter<T> adapter = (TypeAdapter<T>) new EnumTypeAdapter(rawType);
          return adapter;
        }
      };

  @SuppressWarnings("TypeParameterNaming")
  public static <TT> TypeAdapterFactory newFactory(
      final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        return typeToken.equals(type) ? (TypeAdapter<T>) typeAdapter : null;
      }
    };
  }

  @SuppressWarnings("TypeParameterNaming")
  public static <TT> TypeAdapterFactory newFactory(
      final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        return typeToken.getRawType() == type ? (TypeAdapter<T>) typeAdapter : null;
      }

      @Override
      public String toString() {
        return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
      }
    };
  }

  @SuppressWarnings("TypeParameterNaming")
  public static <TT> TypeAdapterFactory newFactory(
      final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        return (rawType == unboxed || rawType == boxed) ? (TypeAdapter<T>) typeAdapter : null;
      }

      @Override
      public String toString() {
        return "Factory[type="
            + boxed.getName()
            + "+"
            + unboxed.getName()
            + ",adapter="
            + typeAdapter
            + "]";
      }
    };
  }

  @SuppressWarnings("TypeParameterNaming")
  public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(
      final Class<TT> base,
      final Class<? extends TT> sub,
      final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        return (rawType == base || rawType == sub) ? (TypeAdapter<T>) typeAdapter : null;
      }

      @Override
      public String toString() {
        return "Factory[type="
            + base.getName()
            + "+"
            + sub.getName()
            + ",adapter="
            + typeAdapter
            + "]";
      }
    };
  }

  /**
   * Returns a factory for all subtypes of {@code typeAdapter}. We do a runtime check to confirm
   * that the deserialized type matches the type requested.
   */
  public static <T1> TypeAdapterFactory newTypeHierarchyFactory(
      final Class<T1> clazz, final TypeAdapter<T1> typeAdapter) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked")
      @Override
      public <T2> TypeAdapter<T2> create(Gson gson, TypeToken<T2> typeToken) {
        final Class<? super T2> requestedType = typeToken.getRawType();
        if (!clazz.isAssignableFrom(requestedType)) {
          return null;
        }
        return (TypeAdapter<T2>)
            new TypeAdapter<T1>() {
              @Override
              public void write(JsonWriter out, T1 value) throws IOException {
                typeAdapter.write(out, value);
              }

              @Override
              public T1 read(JsonReader in) throws IOException {
                T1 result = typeAdapter.read(in);
                if (result != null && !requestedType.isInstance(result)) {
                  throw new JsonSyntaxException(
                      "Expected a "
                          + requestedType.getName()
                          + " but was "
                          + result.getClass().getName()
                          + "; at path "
                          + in.getPreviousPath());
                }
                return result;
              }
            };
      }

      @Override
      public String toString() {
        return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
      }
    };
  }
}
