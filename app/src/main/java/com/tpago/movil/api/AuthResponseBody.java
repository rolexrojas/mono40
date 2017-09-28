package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class AuthResponseBody {

  static AuthResponseBody create(String token) {
    return new AutoValue_AuthResponseBody(token);
  }

  static Function<AuthResponseBody, String> mapperFunc() {
    return new Function<AuthResponseBody, String>() {
      @Override
      public String apply(AuthResponseBody data) throws Exception {
        return data.token();
      }
    };
  }

  public static TypeAdapter<AuthResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_AuthResponseBody.GsonTypeAdapter(gson);
  }

  abstract String token();
}
