package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent {

  void inject(SettingsFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<SettingsComponent> {

    Builder settingsModule(SettingsModule module);
  }
}
