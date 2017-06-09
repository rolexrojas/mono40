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
public abstract class LoanBillBalance extends ProductBillBalance {
  public static TypeAdapter<LoanBillBalance> typeAdapter(Gson gson) {
    return new AutoValue_LoanBillBalance.GsonTypeAdapter(gson);
  }

  @SerializedName("balance") public abstract BigDecimal currentAmount();
  @SerializedName("fee") public abstract BigDecimal periodAmount();

  public enum Option {
    @SerializedName("1") CURRENT,
    @SerializedName("2") PERIOD,
    @SerializedName("3") OTHER
  }
}
