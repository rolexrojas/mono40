package com.tpago.movil;

import com.google.common.base.Optional;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link Optional optionals} to and from
 * JSON.
 *
 * @author hecvasro
 */
public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {

  public static <T> OptionalTypeAdapter<T> create(TypeAdapter<T> typeAdapter) {
    throw new UnsupportedOperationException("not implemented");
  }

  private final TypeAdapter<T> typeAdapter;

  private OptionalTypeAdapter(TypeAdapter<T> typeAdapter) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<T> read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void write(JsonWriter writer, Optional<T> optional) throws IOException {
    throw new UnsupportedOperationException("not implemented");
  }
}
