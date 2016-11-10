package com.gbh.movil.ui.main.accounts;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.FragmentScope;

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

  AccountsModule(@NonNull AccountsScreen screen) {
    this.screen = screen;
  }

  @Provides
  @FragmentScope
  AccountsPresenter providePresenter() {
    return new AccountsPresenter(screen);
  }
}
