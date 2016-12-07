package com.gbh.movil.ui.main.recipients;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.ActivityScope;

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
