package com.mono40.movil.d.ui.main.products.transactions;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.dep.AppComponent;

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
