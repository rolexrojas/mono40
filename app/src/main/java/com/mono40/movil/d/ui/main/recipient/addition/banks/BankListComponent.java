package com.mono40.movil.d.ui.main.recipient.addition.banks;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.ui.main.recipient.addition.AddRecipientComponent;

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
