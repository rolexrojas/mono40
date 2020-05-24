package com.mono40.movil.company.bank;

import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mono40.movil.company.Company;
import com.mono40.movil.util.IncludeHashEquals;
import com.mono40.movil.util.ObjectHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

/**
 * Bank representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Bank extends Company {

  public static Builder builder() {
    return new AutoValue_Bank.Builder();
  }

  Bank() {
  }

  @IncludeHashEquals
  @Override
  public abstract int code();

  public abstract BigDecimal transferCostRate();

  public final BigDecimal calculateTransferCost(BigDecimal amount) {
    return ObjectHelper.checkNotNull(amount, "amount")
      .multiply(this.transferCostRate());
  }

  public abstract Builder toBuilder();

  @AutoValue
  public static abstract class Transaction {

    public static TypeAdapter<Transaction> typeAdapter(Gson gson) {
      return new AutoValue_Bank_Transaction.GsonTypeAdapter(gson);
    }

    Transaction() {
    }

    @Type
    public abstract String type();

    public abstract String description();

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
      Type.SALARY_ADVANCE,
      Type.EXTRA_CREDIT
    })
    public @interface Type {

      String EXTRA_CREDIT = "xcre";
      String SALARY_ADVANCE = "asue";
    }
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder code(int code);

    public abstract Builder id(String id);

    public abstract Builder name(String name);

    public abstract Builder logoTemplate(String logoTemplate);

    public abstract Builder transferCostRate(BigDecimal transferCostRate);

    public abstract Bank build();
  }
}
