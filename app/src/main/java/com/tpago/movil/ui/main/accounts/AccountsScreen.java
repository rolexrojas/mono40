package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Account;

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
}
