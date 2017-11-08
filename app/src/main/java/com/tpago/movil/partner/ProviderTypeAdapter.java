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
public final class ProviderTypeAdapter extends TypeAdapter<Provider> {

  public static ProviderTypeAdapter create(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    return new ProviderTypeAdapter(logoCatalogMapper, gson);
  }

  private final TypeAdapter<Partner> partnerTypeAdapter;

  private ProviderTypeAdapter(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    this.partnerTypeAdapter = PartnerTypeAdapter.create(logoCatalogMapper, gson);
  }

  @Override
  public Provider read(JsonReader reader) throws IOException {
    return (Provider) this.partnerTypeAdapter.read(reader);
  }

  @Override
  public void write(JsonWriter writer, Provider provider) throws IOException {
    this.partnerTypeAdapter.write(writer, provider);
  }
}
