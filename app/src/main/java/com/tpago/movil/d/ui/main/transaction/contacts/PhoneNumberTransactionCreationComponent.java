package com.tpago.movil.d.ui.main.transaction.contacts;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = TransactionCreationComponent.class,
  modules = PhoneNumberTransactionCreationModule.class
)
interface PhoneNumberTransactionCreationComponent {

  void inject(PhoneNumberTransactionCreationFragment fragment);
}
