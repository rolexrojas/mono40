package com.tpago.movil.d.ui.main.transactions.contacts;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.TransactionManager;

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
