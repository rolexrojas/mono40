package com.tpago.movil.d.ui.main.transactions.contacts;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.d.ui.main.transactions.TransactionCreationComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = TransactionCreationComponent.class,
  modules = PhoneNumberTransactionCreationModule.class)
interface PhoneNumberTransactionCreationComponent {
  void inject(PhoneNumberTransactionCreationFragment fragment);
}
