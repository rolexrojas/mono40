package com.tpago.movil.init.signin;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.ApiBridge;
import com.tpago.movil.api.ApiData;
import com.tpago.movil.api.ApiError;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitData;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */

public final class SignInPresenter extends Presenter<SignInPresenter.View> {
  private String emailTextInputContent;
  private boolean isEmailTextInputContentValid = false;
  private String passwordTextInputContent;
  private boolean isPasswordTextInputContentValid = false;

  private boolean shouldForce = false;

  private Disposable disposable = Disposables.disposed();

  @Inject UserStore userStore;
  @Inject InitData initData;
  @Inject Session.Builder sessionBuilder;
  @Inject ApiBridge apiBridge;

  private static String sanitize(String content) {
    return Objects.isNull(content) ? "" : content.trim();
  }

  public SignInPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.checkNotNull(component, "component == null")
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
    view.setSignInButtonEnabled(false);
    view.showSignInButtonAsEnabled(false);
    view.startLoading();
  }

  private void stopLoading() {
    view.stopLoading();
    view.showSignInButtonAsEnabled(isEmailTextInputContentValid && isPasswordTextInputContentValid);
    view.setSignInButtonEnabled(isEmailTextInputContentValid && isPasswordTextInputContentValid);
  }

  final void onEmailTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(emailTextInputContent)) {
      emailTextInputContent = sanitizedContent;
      isEmailTextInputContentValid = Email.isValid(emailTextInputContent);
      updateView();
    }
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(passwordTextInputContent)) {
      passwordTextInputContent = sanitizedContent;
      isPasswordTextInputContentValid = Texts.isNotEmpty(passwordTextInputContent);
      updateView();
    }
  }

  final void onSignInButtonClicked() {
    if (isEmailTextInputContentValid && isPasswordTextInputContentValid) {
      final PhoneNumber phoneNumber = initData.getPhoneNumber();
      final Email email = Email.create(emailTextInputContent);
      disposable = apiBridge.signIn(phoneNumber, email, passwordTextInputContent, shouldForce)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            startLoading();
          }
        })
        .subscribe(new Consumer<HttpResult<ApiData<String>>>() {
          @Override
          public void accept(HttpResult<ApiData<String>> result) throws Exception {
            stopLoading();
            final ApiData<String> data = result.getData();
            if (result.isSuccessful()) {
              // TODO: Both first and last name must be provided by the API.
              sessionBuilder.setToken(data.getValue());
              userStore.set(phoneNumber, email, "First", "Last");
              view.moveToInitScreen();
            } else {
              final ApiError error = data.getError();
              if (error.getCode().equals(ApiError.Code.ALREADY_ASSOCIATED_DEVICE)) {
                view.checkIfUseWantsToForceSignIn();
              } else {
                view.showDialog(
                  R.string.error_title,
                  error.getDescription(),
                  R.string.error_positive_button_text);
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Signing in");
            stopLoading();
            view.showDialog(
              R.string.error_title,
              R.string.error_message,
              R.string.error_positive_button_text);
          }
        });
    } else {
      view.showEmailTextInputAsErratic(!isEmailTextInputContentValid);
      view.showPasswordTextInputAsErratic(!isPasswordTextInputContentValid);
      view.showDialog(
        R.string.sign_in_error_title,
        R.string.sign_in_error_description,
        R.string.sign_in_error_positive_button_text);
    }
  }

  final void onSignInForcingButtonClicked() {
    shouldForce = true;
    onSignInButtonClicked();
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    view.setEmailTextInputContent(emailTextInputContent);
    view.setPasswordTextInputContent(passwordTextInputContent);
    updateView();
  }

  @Override
  public void onViewStopped() {
    super.onViewStopped();
    Disposables.dispose(disposable);
  }

  interface View extends Presenter.View {
    void showDialog(int titleId, String message, int positiveButtonTextId);

    void showDialog(int titleId, int messageId, int positiveButtonTextId);

    void setEmailTextInputContent(String content);

    void showEmailTextInputAsErratic(boolean showAsErratic);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setSignInButtonEnabled(boolean enabled);

    void showSignInButtonAsEnabled(boolean showAsEnabled);

    void startLoading();

    void stopLoading();

    void checkIfUseWantsToForceSignIn();

    void moveToInitScreen();
  }
}
