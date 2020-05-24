package com.mono40.movil.api;

import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.ObjectHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author hecvasro
 */
public final class MapperFailureData implements Function<ResponseBody, FailureData> {

  public static MapperFailureData create(Converter<ResponseBody, FailureData> converter) {
    return new MapperFailureData(converter);
  }

  private final Converter<ResponseBody, FailureData> converter;

  private MapperFailureData(Converter<ResponseBody, FailureData> converter) {
    this.converter = ObjectHelper.checkNotNull(converter, "converter");
  }

  @Override
  public FailureData apply(@NonNull ResponseBody responseBody) throws Exception {
    return this.converter.convert(responseBody);
  }
}
