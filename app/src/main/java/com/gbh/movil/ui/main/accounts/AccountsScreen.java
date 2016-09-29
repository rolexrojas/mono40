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
   * @param succeeded
   *   TODO
   * @param account
   *   TODO
   * @param balance
   *   TODO
   */
  void onBalanceQueried(boolean succeeded, @NonNull Account account, @Nullable Balance balance);

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
