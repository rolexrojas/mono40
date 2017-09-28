package com.tpago.movil.data.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.domain.balance.AvailableBalance;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link AvailableBalance balances} to
 * and from JSON.
 *
 * @author hecvasro
 */
public class AvailableBalanceTypeAdapter extends TypeAdapter<AvailableBalance> {

  public static AvailableBalanceTypeAdapter create() {
    throw new UnsupportedOperationException("not implemented");
  }

  private AvailableBalanceTypeAdapter() {
  }

  @Override
  public AvailableBalance read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, AvailableBalance balance) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
