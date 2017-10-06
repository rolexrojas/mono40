package com.tpago.movil.data.api;

import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class RetrofitApiFailureDataMapper implements Function<ResponseBody, FailureData> {

  static RetrofitApiFailureDataMapper create(Converter<ResponseBody, FailureData> converter) {
    return new RetrofitApiFailureDataMapper(converter);
  }

  private final Converter<ResponseBody, FailureData> converter;

  private RetrofitApiFailureDataMapper(Converter<ResponseBody, FailureData> converter) {
    this.converter = ObjectHelper.checkNotNull(converter, "converter");
  }

  @Override
  public FailureData apply(@NonNull ResponseBody responseBody) throws Exception {
    return this.converter.convert(responseBody);
  }
}
