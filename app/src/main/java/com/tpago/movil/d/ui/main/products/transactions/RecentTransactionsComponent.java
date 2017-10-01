package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.app.ui.BaseActivityModule;
import com.tpago.movil.dep.AppComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  modules = {
    BaseActivityModule.class,
    RecentTransactionsModule.class
  },
  dependencies = AppComponent.class
)
interface RecentTransactionsComponent {

  void inject(RecentTransactionsActivity fragment);
}
