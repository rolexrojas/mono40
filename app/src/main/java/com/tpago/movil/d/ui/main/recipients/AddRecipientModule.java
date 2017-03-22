package com.tpago.movil.d.ui.main.recipients;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.RecipientManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class AddRecipientModule {
  @Provides
  @ActivityScope
  AddRecipientPresenter providePresenter(SchedulerProvider schedulerProvider,
    RecipientManager recipientManager) {
    return new AddRecipientPresenter(schedulerProvider, recipientManager);
  }
}
