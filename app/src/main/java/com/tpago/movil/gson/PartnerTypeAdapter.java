package com.tpago.movil.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.domain.company.LogoUriMapper;
import com.tpago.movil.payment.Partner;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link Partner partners} to and from
 * JSON.
 *
 * @author hecvasro
 */
public class PartnerTypeAdapter extends TypeAdapter<Partner> {

  public static PartnerTypeAdapter create(LogoUriMapper logoUriMapper) {
    throw new UnsupportedOperationException("not implemented");
  }

  private final LogoUriMapper logoUriMapper;

  private PartnerTypeAdapter() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Partner read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, Partner bank) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
