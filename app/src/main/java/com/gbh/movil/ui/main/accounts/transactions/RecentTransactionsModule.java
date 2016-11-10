package com.gbh.movil.ui.main.accounts.transactions;

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
class RecentTransactionsModule {
  private final RecentTransactionsScreen screen;

  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  RecentTransactionsModule(@NonNull RecentTransactionsScreen screen) {
    this.screen = screen;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter() {
    return new RecentTransactionsPresenter(screen);
  }
}
