package com.gbh.movil.ui.main.payments.transactions.contacts;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = TransactionCreationComponent.class)
public interface ContactTransactionCreationComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(ContactTransactionCreationFragment fragment);
}
