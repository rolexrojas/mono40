package com.tpago.movil.d.ui.main;

import com.tpago.movil.d.data.NfcHandler;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.ActivityScope;
import com.tpago.movil.d.ui.AppDialog;

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
