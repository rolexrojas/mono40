package com.tpago.movil.init.signin;

import android.support.v4.util.Pair;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.DApiBridge;
import com.tpago.movil.api.DApiData;
import com.tpago.movil.api.DApiError;
import com.tpago.movil.api.UserData;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitData;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
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
  @Inject DApiBridge depApiBridge;
  @Inject NetworkService networkService;

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
      isEmailTextInputContentValid = Email.isValid(emailTextInputContent);
      updateView();
    }
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(passwordTextInputContent)) {
      passwordTextInputContent = sanitizedContent;
      isPasswordTextInputContentValid = Texts.checkIfNotEmpty(passwordTextInputContent);
      updateView();
    }
  }

  final void onSignInButtonClicked() {
    if (isEmailTextInputContentValid && isPasswordTextInputContentValid) {
      final PhoneNumber phoneNumber = initData.getPhoneNumber();
      final Email email = Email.create(emailTextInputContent);
      disposable = Single.defer(new Callable<SingleSource<Result<Pair<UserData, String>, ErrorCode>>>() {
        @Override
        public SingleSource<Result<Pair<UserData, String>, ErrorCode>> call() throws Exception {
          final Result<Pair<UserData, String>, ErrorCode> result;
          if (networkService.checkIfAvailable()) {
            final HttpResult<DApiData<Pair<UserData, String>>> apiResult = depApiBridge
              .signIn(phoneNumber, email, passwordTextInputContent, shouldForce)
              .blockingGet();
            final DApiData<Pair<UserData, String>> apiResultData = apiResult.getData();
            if (apiResult.isSuccessful()) {
              result = Result.create(apiResultData.getValue());
            } else {
              final DApiError apiResultError = apiResultData.getError();
              ErrorCode resultErrorCode = ErrorCode.UNEXPECTED;
              String resultErrorDescription = apiResultError.getDescription();
              if (apiResultError.getCode().equals(DApiError.Code.ALREADY_ASSOCIATED_DEVICE)) {
                resultErrorCode = ErrorCode.ALREADY_ASSOCIATED_DEVICE;
              }
              result = Result.create(FailureData.create(resultErrorCode, resultErrorDescription));
            }
          } else {
            result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
          }
          return Single.just(result);
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
        .subscribe(new Consumer<Result<Pair<UserData, String>, ErrorCode>>() {
          @Override
          public void accept(Result<Pair<UserData, String>, ErrorCode> result) throws Exception {
            stopLoading();
            if (result.isSuccessful()) {
              final Pair<UserData, String> successData = result.getSuccessData();
              userStore.set(successData.first);
              sessionBuilder.setToken(successData.second);
              view.moveToInitScreen();
            } else {
              final FailureData<ErrorCode> failureData = result.getFailureData();
              switch (failureData.getCode()) {
                case UNAVAILABLE_NETWORK:
                  view.showUnavailableNetworkError();
                  break;
                case ALREADY_ASSOCIATED_DEVICE:
                  view.checkIfUserWantsToForceSignIn();
                  break;
                default:
                  view.showGenericErrorDialog(failureData.getDescription());
                  break;
              }
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);
            stopLoading();
            view.showGenericErrorDialog();
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

    void checkIfUserWantsToForceSignIn();

    void moveToInitScreen();

    void showGenericErrorDialog(String message);
    void showGenericErrorDialog();
    void showUnavailableNetworkError();
  }
}
