package com.tpago.movil.ui.main.products.transactions;

import com.tpago.movil.AppComponent;
import com.tpago.movil.ui.FragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = AppComponent.class)
interface RecentTransactionsComponent {
  void inject(RecentTransactionsActivity fragment);
}
