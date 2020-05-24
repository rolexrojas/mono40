package com.mono40.movil.contract;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.session.User;

/**
 * Contract representation
 * <p>
 * Represents an agreement between an {@link User user} and a {@link Partner provider}.
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Contract {

  public static Builder builder() {
    return new com.mono40.movil.contract.AutoValue_Contract.Builder();
  }

//  private final BalanceStore<PayableBalance> payableBalance;

  Contract() {
  }

  public abstract Partner provider();

  public abstract String number();

//  /**
//   * {@link PayableBalance Balance} that holds the amount that it has pending for payment.
//   */
//  public final BalanceStore<PayableBalance> payableBalance() {
//    throw new UnsupportedOperationException("not implemented");
//  }

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
