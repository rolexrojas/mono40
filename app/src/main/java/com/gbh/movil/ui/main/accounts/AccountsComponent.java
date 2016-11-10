package com.gbh.movil.ui.main.accounts;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = AccountsModule.class, dependencies = MainComponent.class)
interface AccountsComponent {
  void inject(AccountsFragment fragment);
}
