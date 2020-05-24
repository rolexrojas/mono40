package com.mono40.movil.app.ui.main.settings.profile;

import com.mono40.movil.app.ui.fragment.base.BaseFragmentModule;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.picture.PictureCreatorModule;
import com.mono40.movil.app.ui.picture.PictureCreatorPresentationComponentBuilderModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(
  modules = {
    BaseFragmentModule.class,
    PictureCreatorModule.class,
    PictureCreatorPresentationComponentBuilderModule.class,
    ProfileModule.class
  }
)
public interface ProfileComponent {

  void inject(ProfileFragment fragment);
}
