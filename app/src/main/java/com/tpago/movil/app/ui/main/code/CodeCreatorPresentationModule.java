package com.tpago.movil.app.ui.main.code;

import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class CodeCreatorPresentationModule {

  public static CodeCreatorPresentationModule create(CodeCreatorPresentation presentation) {
    return new CodeCreatorPresentationModule(presentation);
  }

  private final CodeCreatorPresentation presentation;

  private CodeCreatorPresentationModule(CodeCreatorPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  CodeCreatorPresenter presenter(
    StringMapper stringMapper,
    AlertManager alertManager,
    CodeCreator codeCreator
  ) {
    if (codeCreator.activeRequestType() == CodeCreator.RequestType.PIN) {
      throw new UnsupportedOperationException("not implemented");
    } else {
      return AltAuthCodeCreatorPresenter.builder()
        .stringMapper(stringMapper)
        .alertManager(alertManager)
        .codeCreator(codeCreator)
        .presentation(this.presentation)
        .build();
    }
  }
}
