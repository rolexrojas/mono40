package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ResetPasswordPINBody {

  public static ResetPasswordPINBody create(String password, String msisdn, String email, String pin) {
    return new AutoValue_ResetPasswordPINBody(msisdn, email, password, pin);
  }

  public static TypeAdapter<ResetPasswordPINBody> typeAdapter(Gson gson) {
    return new AutoValue_ResetPasswordPINBody.GsonTypeAdapter(gson);
  }

  @SerializedName("msisdn")
  public abstract String msisdn();

  @SerializedName("email")
  public abstract String email();

  @SerializedName("password")
  public abstract String password();

  @SerializedName("pin")
  public abstract String pin();

}

