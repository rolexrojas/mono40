package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import dagger.Module;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class AccountsModule {
  private final AccountsScreen screen;

  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  AccountsModule(@NonNull AccountsScreen screen) {
    this.screen = screen;
  }
}
