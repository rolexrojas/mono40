package com.tpago.movil.d.ui.main.recipient.addition.banks;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class BankListModule {

  @Provides
  @FragmentScope
  BankListPresenter providePresenter(SchedulerProvider schedulerProvider, DepApiBridge apiBridge) {
    return new BankListPresenter(schedulerProvider, apiBridge);
  }
}
