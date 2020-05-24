package com.mono40.movil.d.ui.main;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.ui.AppDialog;

import com.mono40.movil.d.ui.main.recipient.index.category.RecipientDrawableStore;

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
