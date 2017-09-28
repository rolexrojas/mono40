package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.dep.Session;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.dep.net.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class RecipientCategoryModule {
  private final Category category;

  RecipientCategoryModule(Category category) {
    this.category = category;
  }

  @Provides
  @FragmentScope
  Category category() {
    return this.category;
  }

  @Provides
  @FragmentScope
  RecipientCategoryPresenter providePresenter(
    StringHelper stringHelper,
    SchedulerProvider schedulerProvider,
    RecipientManager recipientManager,
    SessionManager sessionManager,
    ProductManager productManager,
    PosBridge posBridge,
    UserStore userStore,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    Session session) {
    return new RecipientCategoryPresenter(
      stringHelper,
      recipientManager,
      userStore,
      networkService,
      depApiBridge,
      session.getToken(),
      this.category
    );
  }
}
