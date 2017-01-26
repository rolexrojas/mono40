package com.tpago.movil.ui.main.products;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.ui.FragmentScope;

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
