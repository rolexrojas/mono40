package com.mono40.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class Customer {
  public static boolean checkIfCanBeFetched(State state) {
    return state == State.AFFILIATED || state == State.REGISTERED;
  }

  public static TypeAdapter<Customer> typeAdapter(Gson gson) {
    return new AutoValue_Customer.GsonTypeAdapter(gson);
  }

  public static Customer create(String name) {
    return new AutoValue_Customer(name);
  }

  @SerializedName("Beneficiario") public abstract String getName();

  public enum State {
    @SerializedName("1")NONE,
    @SerializedName("2")AFFILIATED,
    @SerializedName("3")REGISTERED
  }
}
