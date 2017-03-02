package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Bank implements Serializable {
  public static Bank create(int code, String id, String name, String logoUri) {
    return new AutoValue_Bank(code, id, name, logoUri);
  }

  public static TypeAdapter<Bank> typeAdapter(Gson gson) {
    return new AutoValue_Bank.GsonTypeAdapter(gson);
  }

  @SerializedName("bank-code") public abstract int getCode();
  @SerializedName("bank-id") public abstract String getId();
  @SerializedName("bank-name") public abstract String getName();
  @SerializedName("bank-logo-uri") public abstract String logoUri();
}
