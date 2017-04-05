package com.tpago.movil.init.signin;

import android.support.v4.util.Pair;

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
import io.reactivex.functions.Function;
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
    return Objects.checkIfNull(content) ? "" : content.trim();
  }

  public SignInPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
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
      isEmailTextInputContentValid = Email.checkIfValue(emailTextInputContent);
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
        .map(new Function<HttpResult<ApiData<String>>, Pair<Code, String>>() {
          @Override
          public Pair<Code, String> apply(HttpResult<ApiData<String>> result) throws Exception {
            final Code code;
            final String data;
            final ApiData<String> apiData = result.getData();
            if (result.isSuccessful()) {
              code = Code.SUCCESS;
              data = apiData.getValue();
            } else {
              final ApiError apiError = apiData.getError();
              if (apiError.getCode().equals(ApiError.Code.ALREADY_ASSOCIATED_DEVICE)) {
                code = Code.FAILURE_ALREADY_ASSOCIATED_DEVICE;
              } else {
                code = Code.FAILURE_UNKNOWN;
              }
              data = apiError.getDescription();
            }
            return Pair.create(code, data);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            startLoading();
          }
        })
        .subscribe(new Consumer<Pair<Code, String>>() {
          @Override
          public void accept(Pair<Code, String> result) throws Exception {
            stopLoading();
            final Code code = result.first;
            final String data = result.second;
            if (code.equals(Code.SUCCESS)) {
              // TODO: Fetch the first and last name from the API.
              sessionBuilder.setToken(data);
              userStore.set(phoneNumber, email, "Usuario", "tPago");
              view.moveToInitScreen();
            } else if (code.equals(Code.FAILURE_ALREADY_ASSOCIATED_DEVICE)) {
              view.checkIfUserWantsToForceSignIn();
            } else {
              view.showDialog(R.string.error_title, data, R.string.error_positive_button_text);
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

  private enum Code {
    SUCCESS,
    FAILURE_ALREADY_ASSOCIATED_DEVICE,
    FAILURE_UNKNOWN
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

    void checkIfUserWantsToForceSignIn();

    void moveToInitScreen();
  }
}
