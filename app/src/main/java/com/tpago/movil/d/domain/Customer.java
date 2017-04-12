package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Customer {
  public static TypeAdapter<Customer> typeAdapter(Gson gson) {
    return new AutoValue_Customer.GsonTypeAdapter(gson);
  }

  @SerializedName("Beneficiario") public abstract String getName();

  public enum State {
    @SerializedName("1")NONE,
    @SerializedName("2")AFFILIATED,
    @SerializedName("3")REGISTERED
  }
}
