package com.tpago.movil.ui.main.recipients;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.RecipientManager;
import com.tpago.movil.ui.ActivityScope;

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
