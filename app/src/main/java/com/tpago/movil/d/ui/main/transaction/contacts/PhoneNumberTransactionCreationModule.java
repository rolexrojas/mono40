package com.tpago.movil.d.ui.main.transaction.contacts;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.transaction.TransactionCategory;
import com.tpago.movil.net.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class PhoneNumberTransactionCreationModule {
  @Provides
  @FragmentScope
  PhoneNumberTransactionCreationPresenter providePresenter(
    ProductManager productManager,
    Recipient recipient,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    SessionManager sessionManager,
    StringHelper stringHelper,
    TransactionCategory transactionCategory
  ) {
    return new PhoneNumberTransactionCreationPresenter(
      productManager,
      recipient,
      networkService,
      depApiBridge,
      sessionManager.getSession().getAuthToken(),
      stringHelper,
      transactionCategory
    );
  }
}
