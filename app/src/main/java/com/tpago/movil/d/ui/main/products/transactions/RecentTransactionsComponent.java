package com.tpago.movil.d.ui.main.products.transactions;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.dep.AppComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    RecentTransactionsModule.class
  }
)
interface RecentTransactionsComponent {

  void inject(RecentTransactionsActivityBase fragment);
}
