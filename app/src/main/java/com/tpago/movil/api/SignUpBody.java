package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
abstract class SignUpBody {
  static SignUpBody create(
    String imei,
    String phoneNumber,
    String email,
    String password,
    String pin) {
    return new AutoValue_SignUpBody(imei, email, phoneNumber, email, password, pin);
  }

  public static TypeAdapter<SignUpBody> typeAdapter(Gson gson) {
    return new AutoValue_SignUpBody.GsonTypeAdapter(gson);
  }

  abstract String imei();
  abstract String email();
  abstract String msisdn();
  abstract String username();
  abstract String password();
  abstract String pin();
}
