package com.tpago.movil.d.data.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
final class GsonTypeAdapterFactory implements TypeAdapterFactory {
  private final Gson internalGson;

  GsonTypeAdapterFactory(Gson internalGson) {
    this.internalGson = assertNotNull(internalGson, "internalGson == null");
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    return internalGson.getAdapter(type);
  }
}
