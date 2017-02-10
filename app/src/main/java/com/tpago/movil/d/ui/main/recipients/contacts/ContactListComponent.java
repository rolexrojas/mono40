package com.tpago.movil.d.ui.main.recipients.contacts;

import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.main.recipients.AddRecipientComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = AddRecipientComponent.class, modules = ContactListModule.class)
interface ContactListComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(ContactListFragment fragment);
}
