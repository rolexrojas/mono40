package com.tpago.movil.data.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.Currency;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link Currency currencies} to and
 * from JSON.
 *
 * @author hecvasro
 */
public class CurrencyTypeAdapter extends TypeAdapter<Currency> {

  public static CurrencyTypeAdapter create() {
    throw new UnsupportedOperationException("not implemented");
  }

  private CurrencyTypeAdapter() {
  }

  @Override
  public Currency read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, Currency currency) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
