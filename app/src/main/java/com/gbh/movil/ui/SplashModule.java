package com.gbh.movil.ui;

import android.support.annotation.NonNull;

import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.DataLoader;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class SplashModule {
  private final SplashScreen screen;

  SplashModule(@NonNull SplashScreen screen) {
    this.screen = screen;
  }

  @Provides
  @ActivityScope
  SplashPresenter providePresenter(NetworkHelper networkHelper, DataLoader dataLoader) {
    return new SplashPresenter(screen, networkHelper, dataLoader);
  }
}
