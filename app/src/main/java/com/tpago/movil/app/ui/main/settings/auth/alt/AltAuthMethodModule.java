package com.tpago.movil.app.ui.main.settings.auth.alt;

import android.support.annotation.Nullable;

import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.code.CodeCreator;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.data.auth.alt.CodeAltAuthMethodKeyGenerator;
import com.tpago.movil.data.auth.alt.FingerprintAltAuthMethodKeyGenerator;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.util.ObjectHelper;

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
    AltAuthMethodManager altAuthMethodManager,
    CodeAltAuthMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator,
    @Nullable FingerprintAltAuthMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader,
    CodeCreator codeCreator
  ) {
    return AltAuthMethodPresenter.builder()
      .altAuthMethodManager(altAuthMethodManager)
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
