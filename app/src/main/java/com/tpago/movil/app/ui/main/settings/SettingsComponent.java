package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.app.ui.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent {

  void inject(SettingsFragment fragment);
}
