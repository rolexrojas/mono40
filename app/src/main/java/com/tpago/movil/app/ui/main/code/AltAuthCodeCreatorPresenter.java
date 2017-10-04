package com.tpago.movil.app.ui.main.code;

import android.support.annotation.StringRes;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.Code;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.Digit;
import com.tpago.movil.util.DigitValueCreator;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class AltAuthCodeCreatorPresenter extends CodeCreatorPresenter {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final CodeCreator codeCreator;

  private Code code;
  private DigitValueCreator<Code> codeValueCreator;

  private AltAuthCodeCreatorPresenter(Builder builder) {
    super(builder.presentation);

    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.codeCreator = builder.codeCreator;

    this.code = null;
    this.codeValueCreator = Code.creator();
  }

  private void updateSubtitle() {
    @StringRes final int id;
    if (ObjectHelper.isNull(code)) {
      id = R.string.inputCodeForAltAuth;
    } else {
      id = R.string.confirmCodeForAltAuth;
    }
    this.presentation.setSubtitle(this.stringMapper.apply(id));
  }

  private void updateValue() {
    this.presentation.setValue(this.codeValueCreator.toString());
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    this.presentation.setTitle(this.stringMapper.apply(R.string.altAuthMethod));
    this.updateSubtitle();
  }

  @Override
  void onNumPadDigitButtonClicked(@Digit int digit) {
    this.codeValueCreator.addDigit(digit);
    if (this.codeValueCreator.canCreate()) {
      if (ObjectHelper.isNull(this.code)) {
        this.code = this.codeValueCreator.create();
      } else {
        final Code code = this.codeValueCreator.create();
        if (code.equals(this.code)) {
          this.codeCreator.resolveActiveRequest(this.code);
        } else {
          final AlertData data = AlertData.builder(this.stringMapper)
            .title(R.string.weAreSorry)
            .message(R.string.altAuthMethodCodeMismatch)
            .positiveButtonText(R.string.ok)
            .build();
          this.alertManager.show(data);
        }
        this.code = null;
      }
      this.codeValueCreator.clear();
      this.updateSubtitle();
    }

    this.updateValue();
  }

  @Override
  void onNumPadDeleteButtonClicked() {
    this.codeValueCreator.removeLastDigit();
    this.updateValue();
  }

  static final class Builder {

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private CodeCreator codeCreator;
    private CodeCreatorPresentation presentation;

    private Builder() {
    }

    final Builder stringMapper(StringMapper stringMapper) {
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      return this;
    }

    final Builder alertManager(AlertManager alertManager) {
      this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
      return this;
    }

    final Builder codeCreator(CodeCreator codeCreator) {
      this.codeCreator = ObjectHelper.checkNotNull(codeCreator, "codeValueCreator");
      return this;
    }

    final Builder presentation(CodeCreatorPresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final AltAuthCodeCreatorPresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("codeCreator", ObjectHelper.isNull(this.codeCreator))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();

      return new AltAuthCodeCreatorPresenter(this);
    }
  }
}
