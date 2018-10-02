package com.tpago.movil.app.ui.main.code;

import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.util.digit.Digit;

/**
 * @author hecvasro
 */
public abstract class CodeCreatorPresenter extends Presenter<CodeCreatorPresentation> {

  CodeCreatorPresenter(CodeCreatorPresentation presentation) {
    super(presentation);
  }

  abstract void onNumPadDigitButtonClicked(@Digit int digit);

  abstract void onNumPadDeleteButtonClicked();

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();
  }

  @Override
  public void onPresentationPaused() {
    super.onPresentationPaused();
  }
}
