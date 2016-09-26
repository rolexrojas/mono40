package com.tpago.movil.ui.main.accounts;

import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.main.MainComponent;

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
