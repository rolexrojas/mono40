package com.gbh.movil.ui.main.accounts;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(modules = AccountsModule.class, dependencies = MainComponent.class)
interface AccountsComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(AccountsFragment fragment);
}
