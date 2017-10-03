package com.tpago.movil.app.ui.main.settings.profile;

import com.tpago.movil.app.ui.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = ProfileModule.class)
public interface ProfileComponent {

  void inject(ProfileFragment fragment);
}
