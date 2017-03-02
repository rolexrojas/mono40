package com.tpago.movil.dep.ui.main.recipients.partners;

import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.FragmentScope;

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
    SessionManager sessionManager,
    DepApiBridge apiBridge) {
    return new PartnerListPresenter(schedulerProvider, sessionManager, apiBridge);
  }
}
