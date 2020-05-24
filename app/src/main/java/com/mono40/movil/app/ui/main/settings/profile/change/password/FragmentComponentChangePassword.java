package com.mono40.movil.app.ui.main.settings.profile.change.password;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScopeChild
@Subcomponent(modules = FragmentModuleChangePassword.class)
public interface FragmentComponentChangePassword {

  void inject(FragmentChangePassword fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<FragmentComponentChangePassword> {

    Builder changePassword(FragmentModuleChangePassword module);
  }
}
