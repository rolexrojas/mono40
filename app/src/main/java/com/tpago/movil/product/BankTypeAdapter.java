package com.tpago.movil.product;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.LogoUriMapper;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link Bank banks} to and from JSON.
 *
 * @author hecvasro
 */
public class BankTypeAdapter extends TypeAdapter<Bank> {

  public static BankTypeAdapter create(LogoUriMapper logoUriMapper) {
    throw new UnsupportedOperationException("not implemented");
  }

  private final LogoUriMapper logoUriMapper;

  private BankTypeAdapter() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Bank read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, Bank bank) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
