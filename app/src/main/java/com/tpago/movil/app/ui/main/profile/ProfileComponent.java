package com.tpago.movil.app.ui.main.profile;

import com.tpago.movil.app.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = ProfileModule.class)
public interface ProfileComponent {

  void inject(ProfileFragment fragment);
}
