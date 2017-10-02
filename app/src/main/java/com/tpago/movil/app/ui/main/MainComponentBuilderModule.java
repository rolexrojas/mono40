package com.tpago.movil.app.ui.main;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.main.settings.SettingsComponent;
import com.tpago.movil.app.ui.main.settings.SettingsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(
  subcomponents = {
    SettingsComponent.class
  }
)
public abstract class MainComponentBuilderModule {

  @Binds
  @IntoMap
  @ContainerKey(SettingsFragment.class)
  public abstract ComponentBuilder settingsComponentBuilder(
    SettingsComponent.Builder builder
  );
}
