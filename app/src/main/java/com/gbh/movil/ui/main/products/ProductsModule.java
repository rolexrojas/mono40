package com.gbh.movil.ui.main.products;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.EventBus;
import com.gbh.movil.ui.FragmentScope;

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
