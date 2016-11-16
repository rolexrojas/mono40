package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import ch.halarious.core.HalResource;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * TODO
 *
 * @author hecvasro
 */
final class HalConverterFactory extends Converter.Factory {
  private final Gson gson;

  private HalConverterFactory(@NonNull Gson gson) {
    this.gson = gson;
  }

  @NonNull
  public static HalConverterFactory create(@NonNull Gson gson) {
    return new HalConverterFactory(gson);
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
    Retrofit retrofit) {
    if (type instanceof Class<?> && HalResource.class.isAssignableFrom((Class<?>) type)) {
      return new HalResponseBodyConverter<>(gson,
        gson.getAdapter((Class<? extends HalResource>) type));
    } else {
      return null;
    }
  }
}
