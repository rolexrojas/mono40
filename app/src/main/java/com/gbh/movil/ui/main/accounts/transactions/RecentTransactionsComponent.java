package com.gbh.movil.ui.main.accounts.transactions;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.FragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = AppComponent.class)
interface RecentTransactionsComponent {
  void inject(RecentTransactionsActivity fragment);
}
