package com.tpago.movil.dep.ui.main.transactions.contacts;

import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.ui.FragmentScope;

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
