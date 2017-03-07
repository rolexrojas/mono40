package com.tpago.movil.dep.ui.main.products;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.util.EventBus;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class ProductsModule {
  ProductsModule() {
  }

  @Provides
  @FragmentScope
  ProductsPresenter providePresenter(SchedulerProvider schedulerProvider, EventBus eventBus,
    ProductManager productManager, BalanceManager balanceManager) {
    return new ProductsPresenter(schedulerProvider, eventBus, productManager, balanceManager);
  }
}
