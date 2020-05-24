package com.mono40.movil.d.domain;

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

  public static Builder builder() {
    return new AutoValue_CreditCardBillBalance.Builder();
  }

  @SerializedName("current-balance")
  public abstract BigDecimal currentAmount();

  @SerializedName("period-amount")
  public abstract BigDecimal periodAmount();

  @SerializedName("min-amount")
  public abstract BigDecimal minimumAmount();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder dueDate(String d);

    public abstract Builder currentAmount(BigDecimal a);

    public abstract Builder periodAmount(BigDecimal a);

    public abstract Builder minimumAmount(BigDecimal a);

    public abstract CreditCardBillBalance build();
  }

  public enum Option {
    @SerializedName("3")CURRENT,
    @SerializedName("1")PERIOD,
    @SerializedName("2")MINIMUM,
    @SerializedName("4")OTHER
  }
}
