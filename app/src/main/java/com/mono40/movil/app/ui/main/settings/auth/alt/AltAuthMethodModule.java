package com.mono40.movil.app.ui.main.settings.auth.alt;

import androidx.annotation.Nullable;

import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.app.ui.main.code.CodeCreator;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.session.CodeMethodKeyGenerator;
import com.mono40.movil.session.FingerprintMethodKeyGenerator;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class AltAuthMethodModule {

  static AltAuthMethodModule create(AltAuthMethodPresentation presentation) {
    return new AltAuthMethodModule(presentation);
  }

  private final AltAuthMethodPresentation presentation;

  private AltAuthMethodModule(AltAuthMethodPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  AltAuthMethodPresenter presenter(
    SessionManager sessionManager,
    CodeMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator,
    @Nullable FingerprintMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader,
    CodeCreator codeCreator
  ) {
    return AltAuthMethodPresenter.builder()
      .sessionManager(sessionManager)
      .codeAltAuthMethodKeyGeneratorCreator(codeAltAuthMethodKeyGeneratorCreator)
      .fingerprintAltAuthMethodKeyGenerator(fingerprintAltAuthMethodKeyGenerator)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .codeCreator(codeCreator)
      .presentation(this.presentation)
      .build();
  }
}
