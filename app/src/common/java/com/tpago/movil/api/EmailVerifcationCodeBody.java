package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class EmailVerifcationCodeBody {

  public static EmailVerifcationCodeBody create(String msisdn, String email, String code) {
    return new AutoValue_EmailVerifcationCodeBody(msisdn, email, code);
  }

  public static TypeAdapter<EmailVerifcationCodeBody> typeAdapter(Gson gson) {
    return new AutoValue_EmailVerifcationCodeBody.GsonTypeAdapter(gson);
  }

  @SerializedName("msisdn")
  public abstract String msisdn();

  @SerializedName("email")
  public abstract String email();

  @SerializedName("code")
  public abstract String code();

}


