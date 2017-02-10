package com.tpago.movil.d.ui.main.payments;

import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.FragmentScope;

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
    RecipientManager recipientManager, SessionManager sessionManager, ProductManager productManager, PosBridge posBridge) {
    return new PaymentsPresenter(stringHelper, schedulerProvider, recipientManager, sessionManager, productManager, posBridge);
  }
}
