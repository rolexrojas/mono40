package com.mono40.movil.d.ui.main.recipient.addition.contacts;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.ui.main.recipient.addition.AddRecipientComponent;

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
