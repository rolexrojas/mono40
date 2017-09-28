package com.tpago.movil.data.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.domain.balance.PayableBalance;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link PayableBalance balances} to and
 * from JSON.
 *
 * @author hecvasro
 */
public class PayableBalanceTypeAdapter extends TypeAdapter<PayableBalance> {

  public static PayableBalanceTypeAdapter create() {
    throw new UnsupportedOperationException("not implemented");
  }

  private PayableBalanceTypeAdapter() {
  }

  @Override
  public PayableBalance read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, PayableBalance balance) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
