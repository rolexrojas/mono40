package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.DigitHelper;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class PaymentResult {

  public static TypeAdapter<PaymentResult> typeAdapter(Gson gson) {
    return new AutoValue_PaymentResult.GsonTypeAdapter(gson);
  }

  @SerializedName("transaction-id")
  abstract String internalId();

  @Memoized
  public String id() {
    return DigitHelper.removeNonDigits(this.internalId());
  }

  @SerializedName("transaction-message")
  abstract String internalMessage();

  @Memoized
  public String message() {
    return this.internalMessage();
  }
}
