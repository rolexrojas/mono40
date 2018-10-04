package com.tpago.movil.d.ui.main;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.AppDialog;

import com.tpago.movil.d.ui.main.recipient.index.category.RecipientDrawableStore;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public class DepMainModule {

  @Provides
  @ActivityScope
  MainPresenter provideMainPresenter(
    StringHelper stringHelper,
    EventBus eventBus,
    BalanceManager balanceManager,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge
  ) {
    return new MainPresenter(
      stringHelper,
      eventBus,
      balanceManager,
      screenDialogCreator,
      posBridge
    );
  }

  @Provides
  @ActivityScope
  RecipientDrawableStore recipientDrawableStore() {
    return RecipientDrawableStore.create();
  }
}
