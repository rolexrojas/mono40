package com.mono40.movil.d.ui.main.transaction.contacts;

import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationComponent;

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
