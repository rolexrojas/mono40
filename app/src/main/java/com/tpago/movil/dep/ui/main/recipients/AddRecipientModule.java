package com.tpago.movil.dep.ui.main.recipients;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.RecipientManager;

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
