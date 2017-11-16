package com.tpago.movil.partner;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.TemplateToLogoCatalogMapper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class ProviderTypeAdapter extends TypeAdapter<Provider> {

  public static ProviderTypeAdapter create(TemplateToLogoCatalogMapper templateToLogoCatalogMapper, Gson gson) {
    return new ProviderTypeAdapter(templateToLogoCatalogMapper, gson);
  }

  private final TypeAdapter<Partner> partnerTypeAdapter;

  private ProviderTypeAdapter(TemplateToLogoCatalogMapper templateToLogoCatalogMapper, Gson gson) {
    this.partnerTypeAdapter = PartnerTypeAdapter.create(templateToLogoCatalogMapper, gson);
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
