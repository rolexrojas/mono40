package com.mono40.movil.d.ui.main.products;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.dep.net.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ProductsModule {

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
    StringHelper stringHelper
  ) {
    return new ProductsPresenter(
      schedulerProvider,
      eventBus,
      productManager,
      balanceManager,
      networkService,
      depApiBridge,
      stringHelper
    );
  }
}
