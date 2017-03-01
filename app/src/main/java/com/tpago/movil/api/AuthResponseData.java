package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
@AutoValue
abstract class AuthResponseData {
  static AuthResponseData create(String token) {
    return new AutoValue_AuthResponseData(token);
  }

  static Function<AuthResponseData, String> mapperFunc() {
    return new Function<AuthResponseData, String>() {
      @Override
      public String apply(AuthResponseData data) throws Exception {
        return data.token();
      }
    };
  }

  public static TypeAdapter<AuthResponseData> typeAdapter(Gson gson) {
    return new AutoValue_AuthResponseData.GsonTypeAdapter(gson);
  }

  abstract String token();
}
