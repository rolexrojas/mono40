package com.tpago.movil;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * {@link TypeAdapterFactory Factory} for {@link Optional} {@link OptionalTypeAdapter adapters}.
 *
 * @author hecvasro
 */
public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

  public static OptionalTypeAdapterFactory create() {
    throw new UnsupportedOperationException("not implemented");
  }

  private OptionalTypeAdapterFactory() {
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    throw new UnsupportedOperationException("not implemented");
  }
}
