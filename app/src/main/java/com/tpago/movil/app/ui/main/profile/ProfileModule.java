package com.tpago.movil.app.ui.main.profile;

import com.tpago.movil.UserStore;
import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.util.ObjectHelper;

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
  ProfilePresenter createProfilePresenter(UserStore userStore) {
    return ProfilePresenter.builder()
      .userStore(userStore)
      .presentation(this.presentation)
      .build();
  }
}
