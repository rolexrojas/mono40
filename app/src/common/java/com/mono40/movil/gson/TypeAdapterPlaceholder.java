package com.mono40.movil.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mono40.movil.util.Placeholder;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterPlaceholder extends TypeAdapter<Placeholder> {

  static TypeAdapterPlaceholder create() {
    return new TypeAdapterPlaceholder();
  }

  private TypeAdapterPlaceholder() {
  }

  @Override
  public Placeholder read(JsonReader reader) throws IOException {
    reader.skipValue();
    return Placeholder.get();
  }

  @Override
  public void write(JsonWriter writer, Placeholder placeholder) throws IOException {
  }
}
