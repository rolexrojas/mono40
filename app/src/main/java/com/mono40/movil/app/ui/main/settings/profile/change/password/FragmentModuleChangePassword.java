package com.mono40.movil.app.ui.main.settings.profile.change.password;

import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleChangePassword {

  static FragmentModuleChangePassword create(PresentationChangePassword presentation) {
    return new FragmentModuleChangePassword(presentation);
  }

  private final PresentationChangePassword presentation;

  private FragmentModuleChangePassword(PresentationChangePassword presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScopeChild
  PresenterChangePassword presenter(
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return PresenterChangePassword.builder()
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
