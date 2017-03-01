package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
abstract class ApiErrorResponseData {
  static ApiErrorResponseData create(ApiError apiError) {
    return new AutoValue_ApiErrorResponseData(apiError);
  }

  public static TypeAdapter<ApiErrorResponseData> typeAdapter(Gson gson) {
    return new AutoValue_ApiErrorResponseData.GsonTypeAdapter(gson);
  }

  abstract ApiError getError();
}
