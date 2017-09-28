package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author Hector Vasquez
 */
@Deprecated
@AutoValue
public abstract class CreditCardBillBalance extends ProductBillBalance {
  public static TypeAdapter<CreditCardBillBalance> typeAdapter(Gson gson) {
    return new AutoValue_CreditCardBillBalance.GsonTypeAdapter(gson);
  }

  @SerializedName("current-balance") public abstract BigDecimal currentAmount();
  @SerializedName("period-amount") public abstract BigDecimal periodAmount();
  @SerializedName("min-amount") public abstract BigDecimal minimumAmount();

  public enum Option {
    @SerializedName("3") CURRENT,
    @SerializedName("1") PERIOD,
    @SerializedName("2") MINIMUM,
    @SerializedName("4") OTHER
  }
}
