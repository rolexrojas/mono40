package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.Balance;

/**
 * TODO
 *
 * @author hecvasro
 */
interface AccountsScreen {
  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param account
   *   TODO
   */
  void add(@NonNull Account account);

  /**
   * TODO
   *
   * @param account
   *   TODO
   * @param balance
   *   TODO
   */
  void setBalance(@NonNull Account account, @Nullable Balance balance);

  /**
   * TODO
   */
  void showLastTransactionsButton();
}
