package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.paypal.PayPalAccountStore;

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
    RecipientManager recipientManager,
    User user,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    PayPalAccountStore payPalAccountStore
  ) {
    return new RecipientCategoryPresenter(
      user,
      recipientManager,
      depApiBridge,
      this.category,
      stringHelper,
      networkService,
      payPalAccountStore
    );
  }
}
