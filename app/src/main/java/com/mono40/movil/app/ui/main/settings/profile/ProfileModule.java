package com.mono40.movil.app.ui.main.settings.profile;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.picture.PictureCreator;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ProfileModule {

  static ProfileModule create(ProfilePresentation presentation) {
    return new ProfileModule(presentation);
  }

  private final ProfilePresentation presentation;

  private ProfileModule(ProfilePresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  ProfilePresenter createProfilePresenter(
    SessionManager sessionManager,
    PictureCreator pictureCreator
  ) {
    return ProfilePresenter.builder()
      .sessionManager(sessionManager)
      .pictureCreator(pictureCreator)
      .presentation(this.presentation)
      .build();
  }
}
