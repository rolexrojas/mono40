package com.tpago.movil.balance;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.math.BigDecimal;

/**
 * Available {@link Balance balance} representation
 * <p>
 * Holds the amount a holder has available.
 *
 * @author hecvasro
 */
@AutoValue
public abstract class AvailableBalance extends Balance {

  public static Builder builder() {
    return new AutoValue_AvailableBalance.Builder();
  }

  AvailableBalance() {
  }

  public abstract BigDecimal amount();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder queryTime(long queryTime);

    public abstract Builder amount(BigDecimal amount);

    public abstract AvailableBalance build();
  }
}
