package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import ch.halarious.core.HalResource;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * TODO
 *
 * @author hecvasro
 */
final class HalResponseBodyConverter<T extends HalResource> implements Converter<ResponseBody, T> {
  private final Gson gson;
  private final TypeAdapter<T> adapter;

  HalResponseBodyConverter(@NonNull Gson gson, @NonNull TypeAdapter<T> adapter) {
    this.gson = gson;
    this.adapter = adapter;
  }

  @Override
  public T convert(ResponseBody value) throws IOException {
    final JsonReader jsonReader = gson.newJsonReader(value.charStream());
    try {
      return adapter.read(jsonReader);
    } finally {
      value.close();
    }
  }
}
