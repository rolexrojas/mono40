package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class SignUpRequestBody {
  static SignUpRequestBody create(
    String email,
    String imei,
    String msisdn,
    String password,
    String pin) {
    return new AutoValue_SignUpRequestBody(email, imei, msisdn, password, pin, email);
  }

  public static TypeAdapter<SignUpRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_SignUpRequestBody.GsonTypeAdapter(gson);
  }

  abstract String email();
  abstract String imei();
  abstract String msisdn();
  abstract String password();
  abstract String pin();
  abstract String username();
}
