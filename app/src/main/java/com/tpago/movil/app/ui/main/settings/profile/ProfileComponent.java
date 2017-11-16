package com.tpago.movil.app.ui.main.settings.profile;

import com.tpago.movil.app.ui.FragmentModule;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.app.ui.picture.PictureCreatorModule;
import com.tpago.movil.app.ui.picture.PictureCreatorPresentationComponentBuilderModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(
  modules = {
    FragmentModule.class,
    PictureCreatorModule.class,
    PictureCreatorPresentationComponentBuilderModule.class,
    ProfileModule.class
  }
)
public interface ProfileComponent {

  void inject(ProfileFragment fragment);
}
