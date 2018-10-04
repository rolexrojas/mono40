package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.PhoneNumber;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiPhoneNumberState {

  public static TypeAdapter<ApiPhoneNumberState> typeAdapter(Gson gson) {
    return new AutoValue_ApiPhoneNumberState.GsonTypeAdapter(gson);
  }

  ApiPhoneNumberState() {
  }

  @SerializedName("status")
  @PhoneNumber.State
  abstract int state();
}
