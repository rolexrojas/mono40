package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * {@link Event} that represents the expiration of the {@link Balance balance} of an {@link Account
 * account}.
 *
 * @author hecvasro
 */
public class AccountBalanceExpirationEvent extends Event {
  /**
   * TODO
   */
  private final Account account;

  /**
   * TODO
   *
   * @param account
   *   TODO
   */
  public AccountBalanceExpirationEvent(@NonNull Account account) {
    super(EventType.ACCOUNT_BALANCE_EXPIRATION, false);
    this.account = account;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Account getAccount() {
    return account;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object)
      && object instanceof AccountBalanceExpirationEvent
      && ((AccountBalanceExpirationEvent) object).account.equals(account));
  }

  @Override
  public int hashCode() {
    return account.hashCode();
  }

  @Override
  public String toString() {
    return AccountBalanceExpirationEvent.class.getSimpleName() + ":{super=" + super.toString()
      + ",account=" + account + "}";
  }
}
