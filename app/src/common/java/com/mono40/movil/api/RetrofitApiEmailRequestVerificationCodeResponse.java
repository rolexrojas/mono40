package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author ramarante
 */
@AutoValue
public abstract class RetrofitApiEmailRequestVerificationCodeResponse {
  RetrofitApiEmailRequestVerificationCodeResponse() {
  }

  public static TypeAdapter<RetrofitApiEmailRequestVerificationCodeResponse> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiEmailRequestVerificationCodeResponse.GsonTypeAdapter(gson);
  }

  @SerializedName("tittle")
  public abstract String title();

  @SerializedName("content")
  public abstract String message();
}
