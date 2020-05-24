package com.mono40.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract  class ChangePinBody {
  public static ChangePinBody create(String msisdn, String newPin, String oldPin) {
    return new AutoValue_ChangePinBody("spanish", msisdn, newPin, oldPin);
  }

  public static TypeAdapter<ChangePinBody> typeAdapter(Gson gson) {
    return new AutoValue_ChangePinBody.GsonTypeAdapter(gson);
  }

  @SerializedName("lang-pref")
  public abstract String langPref();

  @SerializedName("msisdn")
  public abstract String msisdn();

  @SerializedName("new-pin")
  public abstract String newPin();

  @SerializedName("old-pin")
  public abstract String oldPin();
}

