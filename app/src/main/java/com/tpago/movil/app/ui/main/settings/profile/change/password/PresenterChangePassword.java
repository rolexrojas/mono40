package com.tpago.movil.app.ui.main.settings.profile.change.password;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.lib.Password;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.StringHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * @author hecvasro
 */
final class PresenterChangePassword extends Presenter<PresentationChangePassword> {

  static Builder builder() {
    return new Builder();
  }

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private PresenterChangePassword(Builder builder) {
    super(builder.presentation);

    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  private String password;
  private boolean isPasswordValid = false;

  private String value;
  private boolean isValueValid = false;
  private String valueConfirmation;
  private boolean isValueConfirmationValid = false;

  private Disposable disposable = Disposables.disposed();

  private void updatePresentation() {
    if (this.isPasswordValid) {
      this.presentation.showPasswordInputAsErratic(false);
    }
    if (this.isValueValid) {
      this.presentation.showValueInputAsErratic(false);
    }
    if (this.isValueConfirmationValid) {
      this.presentation.showValueConfirmationAsErratic(false);
    }
    final boolean isValid = this.isPasswordValid
      && this.isValueValid
      && this.isValueConfirmationValid;
    this.presentation.showSubmitButtonAsEnabled(isValid);
  }

  final void onPasswordChanged(String text) {
    this.password = StringHelper.emptyIfNull(text);
    this.isPasswordValid = Password.isValid(this.password);
    this.updatePresentation();
  }

  final void onValueChanged(String text) {
    this.value = StringHelper.emptyIfNull(text);
    this.isValueValid = Password.isValid(this.value);
    this.updatePresentation();
  }

  final void onValueConfirmationChanged(String text) {
    this.valueConfirmation = StringHelper.emptyIfNull(text);
    this.isValueConfirmationValid = this.valueConfirmation.equals(this.value);
    this.updatePresentation();
  }

  final void onSubmitButtonPressed() {
    final boolean isValid = this.isPasswordValid
      && this.isValueValid
      && this.isValueConfirmationValid;
    if (isValid) {
      this.disposable = Single.just(Placeholder.get())
        .delay(1L, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe((p) -> this.alertManager.showAlertForGenericFailure());
    } else {
      this.presentation.showPasswordInputAsErratic(!this.isPasswordValid);
      this.presentation.showValueInputAsErratic(!this.isValueValid);
      this.presentation.showValueConfirmationAsErratic(!this.isValueConfirmationValid);

      this.alertManager.builder()
        .title(this.stringMapper.apply(R.string.change_password_incorrect_format_title))
        .message(this.stringMapper.apply(R.string.change_password_incorrect_format_message))
        .show();
    }
  }

  @Override
  public void onPresentationResumed() {
    this.updatePresentation();
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }

  static final class Builder {

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;
    private PresentationChangePassword presentation;

    private Builder() {
    }

    final Builder stringMapper(StringMapper mapper) {
      this.stringMapper = ObjectHelper.checkNotNull(mapper, "stringMapper");
      return this;
    }

    final Builder alertManager(AlertManager manager) {
      this.alertManager = ObjectHelper.checkNotNull(manager, "alertManager");
      return this;
    }

    final Builder takeoverLoader(TakeoverLoader loader) {
      this.takeoverLoader = ObjectHelper.checkNotNull(loader, "takeoverLoader");
      return this;
    }

    final Builder presentation(PresentationChangePassword presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterChangePassword build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull("presentation"))
        .checkNoMissingProperties();
      return new PresenterChangePassword(this);
    }
  }
}
