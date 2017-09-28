package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.ui.main.recipient.addition.AddRecipientComponent;

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
