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
public abstract class LoanBillBalance extends ProductBillBalance {

  public static TypeAdapter<LoanBillBalance> typeAdapter(Gson gson) {
    return new AutoValue_LoanBillBalance.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_LoanBillBalance.Builder();
  }

  @SerializedName("balance")
  public abstract BigDecimal currentAmount();

  @SerializedName("fee")
  public abstract BigDecimal periodAmount();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder dueDate(String d);

    public abstract Builder currentAmount(BigDecimal a);

    public abstract Builder periodAmount(BigDecimal a);

    public abstract LoanBillBalance build();
  }

  public enum Option {
    @SerializedName("1")CURRENT,
    @SerializedName("2")PERIOD,
    @SerializedName("3")OTHER
  }
}
