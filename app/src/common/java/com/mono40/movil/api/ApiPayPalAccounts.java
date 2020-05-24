package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.paypal.PayPalAccount;

import java.util.List;

@AutoValue
public abstract class ApiPayPalAccounts {

  public static TypeAdapter<ApiPayPalAccounts> typeAdapter(Gson gson) {
    return new AutoValue_ApiPayPalAccounts.GsonTypeAdapter(gson);
  }

  ApiPayPalAccounts() {
  }

  @SerializedName("paypal-accounts")
  public abstract List<PayPalAccount> value();
}
