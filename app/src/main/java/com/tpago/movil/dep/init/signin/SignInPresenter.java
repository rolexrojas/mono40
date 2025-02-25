package com.tpago.movil.dep.init.signin;

import com.tpago.movil.Email;
import com.tpago.movil.lib.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.alert.Alert;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.init.InitComponent;
import com.tpago.movil.dep.init.InitData;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.FingerprintMethodKeyGenerator;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.UnlockMethod;
import com.tpago.movil.session.UnlockMethodKeyGenerator;
import com.tpago.movil.session.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
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
  @Inject @Nullable FingerprintMethodKeyGenerator fingerprintMethodKeyGenerator;

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
      this.isPasswordTextInputContentValid
        = !StringHelper.isNullOrEmpty(this.passwordTextInputContent);

      this.updateView();
    }
  }

  private void handleSuccess(com.tpago.movil.util.Result<User> result) {
    if (result.isSuccessful()) {
      if(result.successData().passwordChanges()){
        this.view.openChangePassword(false, true, true);
      }else {
        this.view.moveToInitScreen();
      }
    } else {
      final com.tpago.movil.util.FailureData failureData = result.failureData();

      final Alert.Builder alertBuilder = this.alertManager.builder();
      if (failureData.code() == Api.FailureCode.ALREADY_ASSOCIATED_DEVICE) {
        alertBuilder
          .title(R.string.dialog_title_already_associated_device)
          .message(R.string.dialog_message_already_associated_device)
          .positiveButtonText(R.string.dialog_positive_text_already_associated_device)
          .positiveButtonAction(this::onSignInForcingButtonClicked)
          .negativeButtonText(R.string.dialog_negative_text_already_associated_device);
      } else {
        alertBuilder.message(failureData.description());
      }
      alertBuilder.show();
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Signing in");
    this.alertManager.showAlertForGenericFailure();
  }

  final void onSignInButtonClicked() {
    if (this.isEmailTextInputContentValid && this.isPasswordTextInputContentValid) {
      final PhoneNumber phoneNumber = this.initData.getPhoneNumber();
      final Email email = Email.create(this.emailTextInputContent);
      final Password password = Password.create(this.passwordTextInputContent);
      final String deviceId = this.deviceIdSupplier.get();

      this.disposable = this.sessionManager.createSession(
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

      this.alertManager.builder()
        .title(R.string.register_form_password_error_title)
        .message(R.string.register_form_password_error_message)
        .show();
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
    DisposableUtil.dispose(this.disposable);

    super.onViewStopped();
  }

  public boolean isFingerprintUnlockMethodEnabled(){
    return this.sessionManager.isUnlockMethodEnabled(UnlockMethod.FINGERPRINT);
  }

  private void onEnableButtonClicked(UnlockMethodKeyGenerator generator) {
    this.disposable = this.sessionManager.enableUnlockMethod(generator)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe((d) -> this.takeoverLoader.show())
            .doOnTerminate(this.takeoverLoader::hide)
            .doOnComplete(()-> this.view.moveToInitScreen())
            .subscribe();
  }

  final void onEnableFingerprintButtonClicked() {
    this.onEnableButtonClicked(this.fingerprintMethodKeyGenerator);
  }

  interface View extends Presenter.View {

    void setEmailTextInputContent(String content);

    void showEmailTextInputAsErratic(boolean showAsErratic);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setSignInButtonEnabled(boolean enabled);

    void showSignInButtonAsEnabled(boolean showAsEnabled);

    void moveToInitScreen();

    void openChangePassword(boolean shouldRequestPIN, boolean shouldCloseSession, boolean shouldDestroySession);
  }
}
