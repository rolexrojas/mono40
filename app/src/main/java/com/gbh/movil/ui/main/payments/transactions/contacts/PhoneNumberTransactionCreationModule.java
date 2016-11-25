package com.gbh.movil.ui.main.payments.transactions.contacts;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.ui.FragmentScope;

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
