package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.DataLoader;
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
   * @param dataLoader
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  RecentTransactionsPresenter providePresenter(DataLoader dataLoader) {
    return new RecentTransactionsPresenter(screen, dataLoader);
  }
}
