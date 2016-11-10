package com.gbh.movil.ui.splash;

import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.RecipientRepo;
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
  InitialDataLoader provideInitialDataLoader(ApiBridge apiBridge, AccountManager accountManager,
    RecipientRepo recipientRepo) {
    return new InitialDataLoader(apiBridge, accountManager, recipientRepo);
  }

  @Provides
  @ActivityScope
  SplashPresenter providePresenter(NetworkHelper networkHelper, SchedulerProvider schedulerProvider,
    InitialDataLoader initialDataLoader) {
    return new SplashPresenter(networkHelper, schedulerProvider, initialDataLoader);
  }
}
