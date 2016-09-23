package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.Balance;
import com.tpago.movil.ui.main.item.Item;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountItem implements Item {
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
