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

  public static AvailableBalance create(BigDecimal amount) {
    throw new UnsupportedOperationException("not implemented");
  }

  AvailableBalance() {
  }

  public abstract BigDecimal amount();

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();
}
