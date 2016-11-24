package com.gbh.movil.ui.main.payments.transactions.contacts;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.ProductManager;
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
    ProductManager productManager) {
    return new PhoneNumberTransactionCreationPresenter(schedulerProvider, productManager);
  }
}
