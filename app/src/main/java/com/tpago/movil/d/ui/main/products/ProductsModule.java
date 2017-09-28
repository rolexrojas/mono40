package com.tpago.movil.d.ui.main.products;

import com.tpago.movil.dep.Session;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.dep.net.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class ProductsModule {
  ProductsModule() {
  }

  @Provides
  @FragmentScope
  ProductsPresenter providePresenter(
    SchedulerProvider schedulerProvider,
    EventBus eventBus,
    ProductManager productManager,
    BalanceManager balanceManager,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    Session session,
    StringHelper stringHelper) {
    return new ProductsPresenter(
      schedulerProvider,
      eventBus,
      productManager,
      balanceManager,
      networkService,
      depApiBridge,
      session.getToken(),
      stringHelper);
  }
}
