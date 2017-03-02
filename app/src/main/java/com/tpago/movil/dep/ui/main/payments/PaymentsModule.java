package com.tpago.movil.dep.ui.main.payments;

import com.tpago.movil.UserStore;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.FragmentScope;

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
  PaymentsPresenter providePresenter(
    StringHelper stringHelper,
    SchedulerProvider schedulerProvider,
    RecipientManager recipientManager,
    SessionManager sessionManager,
    ProductManager productManager,
    PosBridge posBridge,
    UserStore userStore) {
    return new PaymentsPresenter(
      stringHelper,
      schedulerProvider,
      recipientManager,
      sessionManager,
      productManager,
      posBridge,
      userStore);
  }
}
