package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class PaymentResult {
  public static TypeAdapter<PaymentResult> typeAdapter(Gson gson) {
    return new AutoValue_PaymentResult.GsonTypeAdapter(gson);
  }

  @SerializedName("transaction-id") public abstract String id();
  @SerializedName("transaction-message") public abstract String message();
}
