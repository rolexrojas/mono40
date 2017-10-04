package com.tpago.movil.d.ui.main.recipient.addition.partners;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class PartnerListModule {

  @Provides
  @FragmentScope
  PartnerListPresenter providePresenter(
    SchedulerProvider schedulerProvider,
    DepApiBridge apiBridge
  ) {
    return new PartnerListPresenter(schedulerProvider, apiBridge);
  }
}
