package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class PaymentsModule {
  private final PaymentsScreen screen;

  PaymentsModule(@NonNull PaymentsScreen screen) {
    this.screen = screen;
  }

  @Provides
  @FragmentScope
  PaymentsPresenter providePresenter() {
    return new PaymentsPresenter(screen);
  }
}
