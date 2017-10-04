package com.tpago.movil.dep.init.signin;

import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.init.InitComponent;
import com.tpago.movil.dep.init.InitData;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.user.User;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class SignInPresenter extends Presenter<SignInPresenter.View> {

  private static String sanitize(String content) {
    return ObjectHelper.firstNonNull(content, "")
      .trim();
  }

  private String emailTextInputContent;
  private boolean isEmailTextInputContentValid = false;
  private String passwordTextInputContent;
  private boolean isPasswordTextInputContentValid = false;

  private boolean shouldDeactivatePreviousDevice = false;

  private Disposable disposable = Disposables.disposed();

  @Inject AlertManager alertManager;
  @Inject DeviceIdSupplier deviceIdSupplier;
  @Inject InitData initData;
  @Inject SessionManager sessionManager;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;

  public SignInPresenter(View view, InitComponent component) {
    super(view);

    // Injects all the annotated dependencies.
    ObjectHelper.checkNotNull(component, "component")
      .inject(this);
  }

  private void updateView() {
    if (isEmailTextInputContentValid) {
      view.showEmailTextInputAsErratic(false);
    }
    if (isPasswordTextInputContentValid) {
      view.showPasswordTextInputAsErratic(false);
    }
    view.showSignInButtonAsEnabled(isEmailTextInputContentValid && isPasswordTextInputContentValid);
  }

  private void startLoading() {
    this.view.setSignInButtonEnabled(false);
    this.view.showSignInButtonAsEnabled(false);

    this.takeoverLoader.show();
  }

  private void stopLoading() {
    this.takeoverLoader.hide();

    this.view.showSignInButtonAsEnabled(
      this.isEmailTextInputContentValid && this.isPasswordTextInputContentValid
    );
    this.view.setSignInButtonEnabled(
      this.isEmailTextInputContentValid && this.isPasswordTextInputContentValid
    );
  }

  final void onEmailTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(this.emailTextInputContent)) {
      this.emailTextInputContent = sanitizedContent;
      this.isEmailTextInputContentValid = Email.isValid(this.emailTextInputContent);

      this.updateView();
    }
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(this.passwordTextInputContent)) {
      this.passwordTextInputContent = sanitizedContent;
      this.isPasswordTextInputContentValid = Password.isValid(this.passwordTextInputContent);

      this.updateView();
    }
  }

  private void handleSuccess(com.tpago.movil.util.Result<User> result) {
    if (result.isSuccessful()) {
      this.view.moveToInitScreen();
    } else {
      final com.tpago.movil.util.FailureData failureData = result.failureData();

      final AlertData.Builder dataBuilder = AlertData.builder(this.stringMapper);
      if (failureData.code() == FailureData.Code.ALREADY_ASSOCIATED_DEVICE) {
        dataBuilder
          .title(R.string.dialog_title_already_associated_device)
          .message(R.string.dialog_message_already_associated_device)
          .positiveButtonText(R.string.dialog_positive_text_already_associated_device)
          .positiveButtonAction(this::onSignInForcingButtonClicked)
          .negativeButtonText(R.string.dialog_negative_text_already_associated_device);
      } else {
        dataBuilder.message(failureData.description());
      }
      this.alertManager.show(dataBuilder.build());
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Signing in");

    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  final void onSignInButtonClicked() {
    if (this.isEmailTextInputContentValid && this.isPasswordTextInputContentValid) {
      final PhoneNumber phoneNumber = this.initData.getPhoneNumber();
      final Email email = Email.create(this.emailTextInputContent);
      final Password password = Password.create(this.passwordTextInputContent);
      final String deviceId = this.deviceIdSupplier.get();

      this.disposable = this.sessionManager.init(
        phoneNumber,
        email,
        password,
        deviceId,
        this.shouldDeactivatePreviousDevice
      )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.startLoading())
        .doFinally(this::stopLoading)
        .subscribe(this::handleSuccess, this::handleError);
    } else {
      this.view.showEmailTextInputAsErratic(!this.isEmailTextInputContentValid);
      this.view.showPasswordTextInputAsErratic(!this.isPasswordTextInputContentValid);

      final AlertData data = AlertData.builder(this.stringMapper)
        .title(R.string.incorrectCredentials)
        .message(R.string.bothUsernameAndPasswordAreRequired)
        .build();
      this.alertManager.show(data);
    }
  }

  final void onSignInForcingButtonClicked() {
    this.shouldDeactivatePreviousDevice = true;
    this.onSignInButtonClicked();
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();

    this.view.setEmailTextInputContent(this.emailTextInputContent);
    this.view.setPasswordTextInputContent(this.passwordTextInputContent);

    this.updateView();
  }

  @Override
  public void onViewStopped() {
    DisposableHelper.dispose(this.disposable);

    super.onViewStopped();
  }

  interface View extends Presenter.View {

    void setEmailTextInputContent(String content);

    void showEmailTextInputAsErratic(boolean showAsErratic);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setSignInButtonEnabled(boolean enabled);

    void showSignInButtonAsEnabled(boolean showAsEnabled);

    void moveToInitScreen();
  }
}
