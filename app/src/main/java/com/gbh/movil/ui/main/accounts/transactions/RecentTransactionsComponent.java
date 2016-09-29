package com.gbh.movil.ui.main.accounts.transactions;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */

@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = MainComponent.class)
interface RecentTransactionsComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(RecentTransactionsFragment fragment);
}
