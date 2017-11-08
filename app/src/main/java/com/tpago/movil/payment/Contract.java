package com.tpago.movil.payment;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.balance.BalanceStore;
import com.tpago.movil.balance.PayableBalance;
import com.tpago.movil.partner.Partner;
import com.tpago.movil.session.User;

/**
 * Contract representation
 * <p>
 * Represents an agreement between an {@link User user} and a {@link Partner provider}.
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Contract {

  public static Contract builder() {
    throw new UnsupportedOperationException("not implemented");
  }

  private final BalanceStore<PayableBalance> payableBalance;

  Contract() {
    throw new UnsupportedOperationException("not implemented");
  }

  public abstract Partner provider();

  public abstract String number();

  /**
   * {@link PayableBalance Balance} that holds the amount that it has pending for payment.
   */
  public final BalanceStore<PayableBalance> payableBalance() {
    throw new UnsupportedOperationException("not implemented");
  }

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

    public abstract Builder provider(Partner provider);

    public abstract Builder number(String number);

    public abstract Contract build();
  }
}
