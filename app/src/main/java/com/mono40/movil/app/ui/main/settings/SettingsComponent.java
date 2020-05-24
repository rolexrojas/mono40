package com.mono40.movil.app.ui.main.settings;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

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
