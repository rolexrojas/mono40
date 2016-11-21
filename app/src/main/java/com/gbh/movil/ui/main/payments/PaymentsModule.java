package com.gbh.movil.ui.main.payments;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class PaymentsModule {
  PaymentsModule() {
  }

  @Provides
  @FragmentScope
  PaymentsPresenter providePresenter(SchedulerProvider schedulerProvider,
    RecipientManager recipientManager) {
    return new PaymentsPresenter(schedulerProvider, recipientManager);
  }
}
