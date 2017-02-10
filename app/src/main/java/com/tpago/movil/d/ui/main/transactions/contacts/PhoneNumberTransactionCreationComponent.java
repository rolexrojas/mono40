package com.tpago.movil.d.ui.main.transactions.contacts;

import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.main.transactions.TransactionCreationComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = TransactionCreationComponent.class,
  modules = PhoneNumberTransactionCreationModule.class)
interface PhoneNumberTransactionCreationComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(PhoneNumberTransactionCreationFragment fragment);
}
