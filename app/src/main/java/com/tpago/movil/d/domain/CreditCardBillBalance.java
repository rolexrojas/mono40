package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class CreditCardBillBalance extends ProductBillBalance {
  public static TypeAdapter<CreditCardBillBalance> typeAdapter(Gson gson) {
    return new AutoValue_CreditCardBillBalance.GsonTypeAdapter(gson);
  }

  @SerializedName("current-balance") public abstract BigDecimal currentAmount();
  @SerializedName("period-amount") public abstract BigDecimal periodAmount();
  @SerializedName("minimum-amount") public abstract BigDecimal minimumAmount();
}
