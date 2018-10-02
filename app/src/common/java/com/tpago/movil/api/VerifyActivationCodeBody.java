package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class VerifyActivationCodeBody {

  public static VerifyActivationCodeBody create(String msisdn, String ActivationCode) {
    return new AutoValue_VerifyActivationCodeBody(msisdn, ActivationCode);
  }

  public static TypeAdapter<VerifyActivationCodeBody> typeAdapter(Gson gson) {
    return new AutoValue_VerifyActivationCodeBody.GsonTypeAdapter(gson);
  }

  @SerializedName("msisdn")
  public abstract String msisdn();

  @SerializedName("activationCode")
  public abstract String activationCode();

}


