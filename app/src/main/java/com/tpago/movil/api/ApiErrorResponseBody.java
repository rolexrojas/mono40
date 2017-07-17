package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiErrorResponseBody {
  static ApiErrorResponseBody create(DApiError DApiError) {
    return new AutoValue_ApiErrorResponseBody(DApiError);
  }

  public static TypeAdapter<ApiErrorResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_ApiErrorResponseBody.GsonTypeAdapter(gson);
  }

  abstract DApiError getError();
}
