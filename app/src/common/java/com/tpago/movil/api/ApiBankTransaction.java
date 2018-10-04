package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ApiBankTransaction {

  public static TypeAdapter<ApiBankTransaction> typeAdapter(Gson gson) {
    return new AutoValue_ApiBankTransaction.GsonTypeAdapter(gson);
  }

  public static ApiBankTransaction create(String type, String description) {
    return new AutoValue_ApiBankTransaction(type, description);
  }

  ApiBankTransaction() {
  }

  public abstract String type();

  public abstract String description();
}
