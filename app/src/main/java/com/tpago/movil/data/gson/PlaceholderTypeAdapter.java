package com.tpago.movil.data.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.Placeholder;

import java.io.IOException;

/**
 * @author
 */
public final class PlaceholderTypeAdapter extends TypeAdapter<Placeholder> {

  public static PlaceholderTypeAdapter create() {
    return new PlaceholderTypeAdapter();
  }

  private PlaceholderTypeAdapter() {
  }

  @Override
  public void write(JsonWriter writer, Placeholder placeholder) throws IOException {
    writer.nullValue();
  }

  @Override
  public Placeholder read(JsonReader reader) throws IOException {
    reader.nextNull();

    return Placeholder.get();
  }
}
