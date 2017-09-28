package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class AssociateRequestBody {
  static AssociateRequestBody create(String email, String msisdn, String newImei, String password) {
    return new AutoValue_AssociateRequestBody(email, msisdn, newImei, password, email);
  }

  public static TypeAdapter<AssociateRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_AssociateRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("email") abstract String getEmail();
  @SerializedName("msisdn") abstract String getMsisdn();
  @SerializedName("new-imei") abstract String getNewImei();
  @SerializedName("password") abstract String getPassword();
  @SerializedName("username") abstract String getUsername();
}
