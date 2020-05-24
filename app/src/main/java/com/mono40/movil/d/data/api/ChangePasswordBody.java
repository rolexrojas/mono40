package com.mono40.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ChangePasswordBody {

  public static ChangePasswordBody create(String password) {
    return new AutoValue_ChangePasswordBody(password);
  }

  public static TypeAdapter<ChangePasswordBody> typeAdapter(Gson gson) {
    return new AutoValue_ChangePasswordBody.GsonTypeAdapter(gson);
  }

  @SerializedName("password")
  public abstract String password();

}

