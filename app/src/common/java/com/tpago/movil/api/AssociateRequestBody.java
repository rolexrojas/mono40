package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class AssociateRequestBody {
  static AssociateRequestBody create(String email, String msisdn, String newImei, String password) {
    return new AutoValue_AssociateRequestBody(email, msisdn, newImei, password, email);
  }

  public static TypeAdapter<AssociateRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_AssociateRequestBody.GsonTypeAdapter(gson);
  }

  abstract String email();
  abstract String msisdn();
  abstract String newImei();
  abstract String password();
  abstract String username();
}
