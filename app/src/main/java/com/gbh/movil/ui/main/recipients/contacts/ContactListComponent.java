package com.gbh.movil.ui.main.recipients.contacts;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.recipients.AddRecipientComponent;

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
