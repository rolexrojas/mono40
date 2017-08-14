package com.tpago.movil.init.unlock;

import android.support.v4.util.Pair;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.DApiBridge;
import com.tpago.movil.api.DApiData;
import com.tpago.movil.api.UserData;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;
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
public final class UnlockPresenter extends Presenter<UnlockPresenter.View> {
  private static String sanitize(String content) {
    return Objects.checkIfNull(content) ? "" : content.trim();
  }

  private String passwordTextInputContent;
  private boolean isPasswordTextInputContentValid = false;

  private Disposable disposable = Disposables.disposed();

  @Inject UserStore userStore;
  @Inject Session.Builder sessionBuilder;
  @Inject DApiBridge depApiBridge;
  @Inject NetworkService networkService;

  UnlockPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
  }

  private void updateView() {
    if (isPasswordTextInputContentValid) {
      view.showPasswordTextInputAsErratic(false);
    }
    view.showUnlockButtonAsEnabled(isPasswordTextInputContentValid);
  }

  private void startLoading() {
    view.setUnlockButtonEnabled(false);
    view.showUnlockButtonAsEnabled(false);
    view.startLoading();
  }

  private void stopLoading() {
    view.stopLoading();
    view.showUnlockButtonAsEnabled(isPasswordTextInputContentValid);
    view.setUnlockButtonEnabled(isPasswordTextInputContentValid);
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(passwordTextInputContent)) {
      passwordTextInputContent = sanitizedContent;
      isPasswordTextInputContentValid = Texts.checkIfNotEmpty(passwordTextInputContent);
      updateView();
    }
  }

  final void onUnlockButtonClicked() {
    if (isPasswordTextInputContentValid) {
      final User user = userStore.get();
      final PhoneNumber phoneNumber = user.phoneNumber();
      final Email email = user.email();
      disposable = Single.defer(new Callable<SingleSource<Result<Pair<UserData, String>, ErrorCode>>>() {
        @Override
        public SingleSource<Result<Pair<UserData, String>, ErrorCode>> call() throws Exception {
            final Result<Pair<UserData, String>, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final HttpResult<DApiData<Pair<UserData, String>>> apiResult = depApiBridge
                .signIn(phoneNumber, email, passwordTextInputContent, false)
                .blockingGet();
              final DApiData<Pair<UserData, String>> apiResultData = apiResult.getData();
              if (apiResult.isSuccessful()) {
                result = Result.create(apiResultData.getValue());
              } else {
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    apiResultData.getError().getDescription())
                );
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
      view.showPasswordTextInputAsErratic(true);
      view.showDialog(
        R.string.sign_in_error_title,
        R.string.sign_in_error_description,
        R.string.sign_in_error_positive_button_text);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final User user = userStore.get();
    view.setAvatarImageContent(user.avatar().getFile());
    view.setTitleLabelContent(user.firstName());
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

    void setAvatarImageContent(File file);

    void setTitleLabelContent(String content);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setUnlockButtonEnabled(boolean enabled);

    void showUnlockButtonAsEnabled(boolean showAsEnabled);

    void startLoading();

    void stopLoading();

    void moveToInitScreen();

    void showGenericErrorDialog(String message);
    void showGenericErrorDialog();
    void showUnavailableNetworkError();
  }
}
