package com.tpago.movil.dep.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class SignInRequestBody {
  static SignInRequestBody create(String email, String imei, String msisdn, String password) {
    return new AutoValue_SignInRequestBody(email, imei, msisdn, password, email);
  }

  public static TypeAdapter<SignInRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_SignInRequestBody.GsonTypeAdapter(gson);
  }

  abstract String email();
  abstract String imei();
  abstract String msisdn();
  abstract String password();
  abstract String username();
}
