package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.ui.FragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = AppComponent.class)
interface RecentTransactionsComponent {
  void inject(RecentTransactionsActivity fragment);
}
