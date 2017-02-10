package com.tpago.movil.d.ui.main.products;

import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.FragmentScope;

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
