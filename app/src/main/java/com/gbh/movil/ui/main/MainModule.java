package com.gbh.movil.ui.main;

import com.gbh.movil.data.NfcHandler;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.ui.ActivityScope;
import com.gbh.movil.ui.AppDialog;

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
  MainPresenter provideMainPresenter(StringHelper stringHelper, EventBus eventBus,
    BalanceManager balanceManager, AppDialog.Creator screenDialogCreator, NfcHandler nfcHandler) {
    return new MainPresenter(stringHelper, eventBus, balanceManager, screenDialogCreator,
      nfcHandler);
  }
}
