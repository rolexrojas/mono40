package com.mono40.movil.app.ui.main.settings.profile.change;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ContainerKey;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.main.settings.profile.change.password.FragmentChangePassword;
import com.mono40.movil.app.ui.main.settings.profile.change.password.FragmentComponentChangePassword;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(subcomponents = {
  FragmentComponentChangePassword.class
})
public abstract class FragmentModuleComponentBuilderChange {

  @Binds
  @IntoMap
  @ContainerKey(FragmentChangePassword.class)
  @FragmentScope
  @FragmentQualifier
  public abstract ComponentBuilder password(FragmentComponentChangePassword.Builder builder);
}
