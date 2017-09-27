package com.tpago.movil.app.ui.main.settings.index;

import com.tpago.movil.app.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = SettingsIndexModule.class)
public interface SettingsIndexComponent {

  void inject(SettingsIndexFragment fragment);
}
