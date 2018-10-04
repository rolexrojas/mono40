package com.tpago.movil.d.ui.main.recipient.addition.banks;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = AddRecipientComponent.class,
  modules = BankListModule.class
)
interface BankListComponent {

  void inject(BankListFragment fragment);
}
