package com.tpago.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.DataLoader;
import com.tpago.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

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

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  AccountsPresenter providePresenter(DataLoader dataLoader) {
    return new AccountsPresenter(screen, dataLoader);
  }
}
