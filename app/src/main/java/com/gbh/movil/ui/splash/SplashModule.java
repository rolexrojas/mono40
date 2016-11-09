package com.gbh.movil.ui.splash;

import com.gbh.movil.domain.NetworkHelper;
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
  SplashPresenter providePresenter(NetworkHelper networkHelper) {
    return new SplashPresenter(networkHelper);
  }
}
