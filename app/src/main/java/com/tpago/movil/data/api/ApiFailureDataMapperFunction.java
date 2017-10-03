package com.tpago.movil.data.api;

import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class ApiFailureDataMapperFunction implements Function<ResponseBody, FailureData> {

  private final Converter<ResponseBody, FailureData> converter;

  private ApiFailureDataMapperFunction(Converter<ResponseBody, FailureData> converter) {
    this.converter = ObjectHelper.checkNotNull(converter, "converter");
  }

  @Override
  public FailureData apply(@NonNull ResponseBody responseBody) throws Exception {
    return this.converter.convert(responseBody);
  }
}
