package com.tpago.movil.ui.main;

import android.support.annotation.NonNull;

import com.tpago.movil.data.MessageHelper;
import com.tpago.movil.data.net.NetworkHelper;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.DataLoader;
import com.tpago.movil.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class MainModule {
  private final MainScreen screen;

  MainModule(@NonNull MainScreen screen) {
    this.screen = screen;
  }

  /**
   * TODO
   *
   * @param messageHelper
   *   TODO
   * @param networkHelper
   *   TODO
   * @param dataLoader
   *   TODO
   * @param balanceManager
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  MainPresenter provideMainPresenter(MessageHelper messageHelper, NetworkHelper networkHelper,
    DataLoader dataLoader, BalanceManager balanceManager) {
    return new MainPresenter(screen, messageHelper, networkHelper, dataLoader, balanceManager);
  }
}
