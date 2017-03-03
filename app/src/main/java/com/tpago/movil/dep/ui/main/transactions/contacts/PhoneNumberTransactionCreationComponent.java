package com.tpago.movil.dep.ui.main.transactions.contacts;

import com.tpago.movil.dep.ui.FragmentScope;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationComponent;

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
