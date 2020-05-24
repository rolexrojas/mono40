package com.mono40.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mono40.movil.Currency;
import com.mono40.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterCurrency extends TypeAdapter<Currency> {

  private static final String VALUE_APP = "RD$";
  private static final String VALUE_API = "DOP";

  private static String toAppValue(String value) {
    if (VALUE_API.equals(value)) {
      return VALUE_APP;
    }
    return value;
  }

  private static String toApiValue(String value) {
    if (VALUE_APP.equals(value)) {
      return VALUE_API;
    }
    return value;
  }

  public static TypeAdapterCurrency create(Gson gson) {
    return new TypeAdapterCurrency(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterCurrency(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Currency read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return Currency.create(toAppValue(this.stringTypeAdapter.read(reader)));
  }

  @Override
  public void write(JsonWriter writer, Currency currency) throws IOException {
    if (ObjectHelper.isNull(currency)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, toApiValue(currency.value()));
    }
  }
}
