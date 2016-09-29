package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountItem {
  /**
   * TODO
   */
  protected final Account account;

  /**
   * TODO
   */
  protected Balance balance;

  /**
   * TODO
   *
   * @param account
   *   TODO
   */
  AccountItem(@NonNull Account account) {
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

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public Balance getBalance() {
    return balance;
  }

  /**
   * TODO
   *
   * @param balance
   *   TODO
   */
  public void setBalance(@Nullable Balance balance) {
    this.balance = balance;
  }
}
