package com.tpago.movil.app.ui.main.settings.profile.change;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.main.settings.profile.change.password.FragmentChangePassword;
import com.tpago.movil.app.ui.main.settings.profile.change.password.FragmentComponentChangePassword;

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
