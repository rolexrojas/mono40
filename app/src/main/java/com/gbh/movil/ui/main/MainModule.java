package com.gbh.movil.ui.main;

import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class MainModule {
  @Provides
  @ActivityScope
  MainPresenter provideMainPresenter(MessageHelper messageHelper, EventBus eventBus,
    BalanceManager balanceManager) {
    return new MainPresenter(messageHelper, eventBus, balanceManager);
  }
}
