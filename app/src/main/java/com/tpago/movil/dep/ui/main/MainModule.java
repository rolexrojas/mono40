package com.tpago.movil.dep.ui.main;

import com.tpago.movil.dep.data.NfcHandler;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.ui.ActivityScope;
import com.tpago.movil.dep.ui.AppDialog;

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
