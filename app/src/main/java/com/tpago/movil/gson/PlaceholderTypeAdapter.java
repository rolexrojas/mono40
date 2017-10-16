package com.tpago.movil.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.Placeholder;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class PlaceholderTypeAdapter extends TypeAdapter<Placeholder> {

  static PlaceholderTypeAdapter create() {
    return new PlaceholderTypeAdapter();
  }

  private PlaceholderTypeAdapter() {
  }

  @Override
  public void write(JsonWriter writer, Placeholder placeholder) throws IOException {
  }

  @Override
  public Placeholder read(JsonReader reader) throws IOException {
    reader.skipValue();

    return Placeholder.get();
  }
}
