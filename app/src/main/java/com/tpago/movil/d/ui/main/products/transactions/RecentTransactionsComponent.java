package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.dep.AppComponent;
import com.tpago.movil.app.ui.FragmentScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecentTransactionsModule.class, dependencies = AppComponent.class)
interface RecentTransactionsComponent {
  void inject(RecentTransactionsActivity fragment);
}
