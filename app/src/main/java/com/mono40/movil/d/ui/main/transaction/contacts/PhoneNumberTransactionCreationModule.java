package com.mono40.movil.d.ui.main.transaction.contacts;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.main.transaction.TransactionCategory;
import com.mono40.movil.dep.net.NetworkService;

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
    StringHelper stringHelper,
    TransactionCategory transactionCategory
  ) {
    return new PhoneNumberTransactionCreationPresenter(
      productManager,
      recipient,
      networkService,
      depApiBridge,
      stringHelper,
      transactionCategory
    );
  }
}
