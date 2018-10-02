package com.tpago.movil.api;

import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author hecvasro
 */
final class MapperFailureData implements Function<ResponseBody, FailureData> {

  static MapperFailureData create(Converter<ResponseBody, FailureData> converter) {
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
