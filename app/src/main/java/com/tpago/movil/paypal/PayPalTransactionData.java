package com.tpago.movil.paypal;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

@AutoValue
public abstract class PayPalTransactionData {

  public static TypeAdapter<PayPalTransactionData> typeAdapter(Gson gson) {
    return new AutoValue_PayPalTransactionData.GsonTypeAdapter(gson);
  }

  PayPalTransactionData() {
  }

  @SerializedName("paypal-comission-fee")
  public abstract BigDecimal payPalFee();

  @SerializedName("gcs-fee")
  public abstract BigDecimal gcsFee();

  @SerializedName("bank-service-fee")
  public abstract BigDecimal bankFee();

  @SerializedName("customer-service-fee")
  public abstract BigDecimal customerFee();

  @Memoized
  public BigDecimal fee() {
    return this.gcsFee();
  }

  @SerializedName("paypal-exchange-rate")
  public abstract BigDecimal rate();

  @SerializedName("paypal-tax")
  public abstract BigDecimal tax();

  @SerializedName("total-amount")
  public abstract BigDecimal total();
}
