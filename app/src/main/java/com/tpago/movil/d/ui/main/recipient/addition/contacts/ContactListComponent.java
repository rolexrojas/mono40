package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(
  dependencies = AddRecipientComponent.class,
  modules = ContactListModule.class
)
interface ContactListComponent {

  void inject(ContactListFragment fragment);

  CompanyHelper logoFactory();
}
