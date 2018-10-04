package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Code;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.paypal.PayPalAccount;

import java.math.BigDecimal;

import io.reactivex.annotations.Nullable;

@AutoValue
public abstract class ApiPayPalTransactionBody {

  public static TypeAdapter<ApiPayPalTransactionBody> typeAdapter(Gson gson) {
    return new AutoValue_ApiPayPalTransactionBody.GsonTypeAdapter(gson);
  }

  static Builder builder() {
    return new AutoValue_ApiPayPalTransactionBody.Builder();
  }

  ApiPayPalTransactionBody() {
  }

  @SerializedName("paypal-account")
  public abstract PayPalAccount recipient();

  @SerializedName("funding-account")
  public abstract Product paymentMethod();

  @SerializedName("amount")
  public abstract BigDecimal amount();

  @SerializedName("paypal-comission-fee")
  @Nullable
  public abstract BigDecimal payPalFee();

  @SerializedName("gcs-fee")
  @Nullable
  public abstract BigDecimal gcsFee();

  @SerializedName("bank-service-fee")
  @Nullable
  public abstract BigDecimal bankFee();

  @SerializedName("customer-service-fee")
  @Nullable
  public abstract BigDecimal customerFee();

  @SerializedName("paypal-exchange-rate")
  @Nullable
  public abstract BigDecimal rate();

  @SerializedName("paypal-tax")
  @Nullable
  public abstract BigDecimal tax();

  @SerializedName("total-amount")
  @Nullable
  public abstract BigDecimal total();

  @SerializedName("pin")
  @Nullable
  public abstract String pin();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    public abstract Builder recipient(PayPalAccount recipient);

    public abstract Builder paymentMethod(Product paymentMethod);

    public abstract Builder amount(BigDecimal amount);

    public abstract Builder payPalFee(@Nullable BigDecimal payPalFee);

    public abstract Builder gcsFee(@Nullable BigDecimal gcsFee);

    public abstract Builder bankFee(@Nullable BigDecimal bankFee);

    public abstract Builder customerFee(@Nullable BigDecimal customerFee);

    public abstract Builder rate(@Nullable BigDecimal rate);

    public abstract Builder tax(@Nullable BigDecimal tax);

    public abstract Builder total(@Nullable BigDecimal total);

    public abstract Builder pin(@Nullable String pin);

    public abstract ApiPayPalTransactionBody build();
  }
}
