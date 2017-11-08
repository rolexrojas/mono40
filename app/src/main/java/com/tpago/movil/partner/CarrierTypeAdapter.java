package com.tpago.movil.partner;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.LogoCatalogMapper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class CarrierTypeAdapter extends TypeAdapter<Carrier> {

  public static CarrierTypeAdapter create(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    return new CarrierTypeAdapter(logoCatalogMapper, gson);
  }

  private final TypeAdapter<Partner> partnerTypeAdapter;

  private CarrierTypeAdapter(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    this.partnerTypeAdapter = PartnerTypeAdapter.create(logoCatalogMapper, gson);
  }

  @Override
  public Carrier read(JsonReader reader) throws IOException {
    return (Carrier) this.partnerTypeAdapter.read(reader);
  }

  @Override
  public void write(JsonWriter writer, Carrier carrier) throws IOException {
    this.partnerTypeAdapter.write(writer, carrier);
  }
}
