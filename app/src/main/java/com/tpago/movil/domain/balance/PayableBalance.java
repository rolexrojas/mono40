package com.tpago.movil.domain.balance;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.base.Optional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Payable {@link Balance balance} representation
 * <p>
 * Holds the amount a holder has pending for payment.
 *
 * @author hecvasro
 */
@AutoValue
public abstract class PayableBalance extends Balance {

  public static Builder builder() {
    throw new UnsupportedOperationException("not implemented");
  }

  PayableBalance() {
  }

  public abstract Date dueDate();

  /**
   * Amount pending from the current and previous periods.
   */
  public abstract BigDecimal totalAmount();

  /**
   * Amount pending from the previous period.
   */
  public abstract BigDecimal periodAmount();

  /**
   * Minimum amount that can be paid.
   */
  public abstract Optional<BigDecimal> minimumAmount();

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

    public abstract Builder dueDate(Date dueDate);

    public abstract Builder totalAmount(BigDecimal totalAmount);

    public abstract Builder periodAmount(BigDecimal periodAmount);

    public abstract Builder minimumAmount(Optional<BigDecimal> minimumAmount);

    public abstract PayableBalance build();
  }
}
