package com.tpago.movil.ui.main.payments;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.data.StringHelper;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.RecipientManager;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.FragmentScope;

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
  PaymentsPresenter providePresenter(StringHelper stringHelper, SchedulerProvider schedulerProvider,
    RecipientManager recipientManager, SessionManager sessionManager, ProductManager productManager) {
    return new PaymentsPresenter(stringHelper, schedulerProvider, recipientManager, sessionManager, productManager);
  }
}
