package com.tpago.movil.currency;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class CurrencyTypeAdapter extends TypeAdapter<Currency> {

  private static final String CURRENCY_READABLE_DOP = "RD$";
  private static final String CURRENCY_WRITABLE_DOP = "DOP";

  public static CurrencyTypeAdapter create(Gson gson) {
    return new CurrencyTypeAdapter(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private CurrencyTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Currency read(JsonReader reader) throws IOException {
    Currency currency = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      String value = this.stringTypeAdapter.read(reader);
      if (value.equals(CURRENCY_WRITABLE_DOP)) {
        value = CURRENCY_READABLE_DOP;
      }
      currency = Currency.create(value);
    }
    return currency;
  }

  @Override
  public void write(JsonWriter writer, Currency currency) throws IOException {
    if (ObjectHelper.isNull(currency)) {
      writer.nullValue();
    } else {
      String value = currency.value();
      if (value.equals(CURRENCY_READABLE_DOP)) {
        value = CURRENCY_WRITABLE_DOP;
      }
      this.stringTypeAdapter.write(writer, value);
    }
  }
}
