package com.tpago.movil.ui.main.transactions.contacts;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.TransactionManager;
import com.tpago.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class PhoneNumberTransactionCreationModule {
  @Provides
  @FragmentScope
  PhoneNumberTransactionCreationPresenter providePresenter(SchedulerProvider schedulerProvider,
    ProductManager productManager, TransactionManager transactionManager, Recipient recipient) {
    return new PhoneNumberTransactionCreationPresenter(schedulerProvider, productManager,
      transactionManager, recipient);
  }
}
