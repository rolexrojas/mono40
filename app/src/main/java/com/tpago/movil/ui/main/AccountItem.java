package com.tpago.movil.ui.main;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Account;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AccountItem implements Item {
  /**
   * TODO
   */
  protected final Account account;

  /**
   * TODO
   *
   * @param account
   *   TODO
   */
  public AccountItem(@NonNull Account account) {
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
  public int hashCode() {
    return account.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    return object != null && (super.equals(object) || (object instanceof AccountItem) &&
      ((AccountItem) object).account.equals(account));
  }
}
