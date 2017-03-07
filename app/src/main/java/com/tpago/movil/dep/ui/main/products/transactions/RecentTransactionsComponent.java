package com.tpago.movil.dep.ui.main.products.transactions;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.app.FragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = AppComponent.class)
interface RecentTransactionsComponent {
  void inject(RecentTransactionsActivity fragment);
}
