package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author ramarante
 */
@AutoValue
public abstract class RetrofitApiEmailVerificationBody {
  RetrofitApiEmailVerificationBody() {
  }

  public static TypeAdapter<RetrofitApiEmailVerificationBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiEmailVerificationBody.GsonTypeAdapter(gson);
  }

  @SerializedName("is-verified-email")
  abstract int isVerified();
}
