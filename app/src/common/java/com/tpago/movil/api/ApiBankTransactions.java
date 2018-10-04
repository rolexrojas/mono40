package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.IncludeHashEquals;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiBankTransactions {

  public static TypeAdapter<ApiBankTransactions> typeAdapter(Gson gson) {
    return new AutoValue_ApiBankTransactions.GsonTypeAdapter(gson);
  }

  ApiBankTransactions() {
  }

  @SerializedName("bank-code")
  @IncludeHashEquals
  abstract int code();

  @SerializedName("transactions")
  abstract List<ApiBankTransaction> items();

  @Memoized
  boolean hasItems() {
    return !this.items()
      .isEmpty();
  }
}
