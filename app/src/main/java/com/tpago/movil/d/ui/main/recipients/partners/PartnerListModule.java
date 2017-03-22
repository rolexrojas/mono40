package com.tpago.movil.d.ui.main.recipients.partners;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;

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
