package com.gbh.movil.ui.splash;

import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.InitialDataLoader;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class SplashModule {
  SplashModule() {
  }

  @Provides
  @ActivityScope
  InitialDataLoader provideInitialDataLoader(ApiBridge apiBridge, ProductManager productManager,
    RecipientManager recipientManager) {
    return new InitialDataLoader(apiBridge, productManager, recipientManager);
  }

  @Provides
  @ActivityScope
  SplashPresenter providePresenter(NetworkHelper networkHelper, SchedulerProvider schedulerProvider,
    InitialDataLoader initialDataLoader) {
    return new SplashPresenter(networkHelper, schedulerProvider, initialDataLoader);
  }
}
