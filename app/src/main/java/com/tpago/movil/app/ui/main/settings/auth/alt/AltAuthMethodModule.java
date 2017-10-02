package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.app.ui.TakeoverLoader;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.data.auth.alt.CodeAltAuthMethodKeyGenerator;
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
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return AltAuthMethodPresenter.builder()
      .altAuthMethodManager(altAuthMethodManager)
      .codeAltAuthMethodKeyGeneratorCreator(codeAltAuthMethodKeyGeneratorCreator)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
