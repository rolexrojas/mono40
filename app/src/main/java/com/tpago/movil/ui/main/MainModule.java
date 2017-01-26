package com.tpago.movil.ui.main;

import com.tpago.movil.data.NfcHandler;
import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.ui.AppDialog;

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
