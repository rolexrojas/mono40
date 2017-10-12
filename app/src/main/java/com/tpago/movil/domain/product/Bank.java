package com.tpago.movil.domain.product;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.LogoCatalog;
import com.tpago.movil.util.ObjectHelper;

import java.math.BigDecimal;

/**
 * Bank representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Bank extends Company implements Comparable<Bank> {

  public static Builder builder() {
    return new AutoValue_Bank.Builder();
  }

  Bank() {
  }

  public abstract BigDecimal transferCostRate();

  public final BigDecimal calculateTransferCost(BigDecimal amount) {
    return ObjectHelper.checkNotNull(amount, "amount")
      .multiply(this.transferCostRate());
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @Override
  public int compareTo(@NonNull Bank that) {
    throw new UnsupportedOperationException("not implemented");
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder id(String id);

    public abstract Builder code(int code);

    public abstract Builder name(String name);

    public abstract Builder logoTemplate(String logoTemplate);

    public abstract Builder logoCatalog(LogoCatalog logoHolder);

    public abstract Builder transferCostRate(BigDecimal transferCostRate);

    public abstract Bank build();
  }
}
