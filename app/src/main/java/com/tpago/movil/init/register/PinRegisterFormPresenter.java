package com.tpago.movil.init.register;

import android.support.v4.util.Pair;
import com.tpago.movil.Digit;
import com.tpago.movil.Pin;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.DApiBridge;
import com.tpago.movil.api.DApiData;
import com.tpago.movil.api.DApiError;
import com.tpago.movil.api.UserData;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
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
public final class PinRegisterFormPresenter extends Presenter<PinRegisterFormPresenter.View> {
  private final Pin.Builder builder;

  private Disposable disposable = Disposables.disposed();

  @Inject
  UserStore userStore;
  @Inject
  DApiBridge apiBridge;
  @Inject
  Session.Builder sessionBuilder;
  @Inject
  NetworkService networkService;
  @Inject
  StringHelper stringHelper;

  @Inject
  RegisterData registerData;

  PinRegisterFormPresenter(View view, RegisterComponent component) {
    super(view);
    // Injects all annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
    // Initializes the presenter.
    builder = new Pin.Builder();
  }

  private void updateView() {
    view.setTextInputContent(builder.getMaskedValue());
    if (builder.canBuild()) {
      final Pin pin = builder.build();
      disposable = Single.defer(new Callable<SingleSource<Result<Pair<UserData, String>, ErrorCode>>>() {
        @Override
        public SingleSource<Result<Pair<UserData, String>, ErrorCode>> call() throws Exception {
          final Result<Pair<UserData, String>, ErrorCode> result;
          if (networkService.checkIfAvailable()) {
            final HttpResult<DApiData<Pair<UserData, String>>> registerResult = apiBridge.signUp(
              registerData.getPhoneNumber(),
              registerData.getEmail(),
              registerData.getPassword(),
              pin)
              .blockingGet();
            final DApiData<Pair<UserData, String>> resultData = registerResult.getData();
            if (registerResult.isSuccessful()) {
              result = Result.create(resultData.getValue());
            } else {
              final DApiError apiError = resultData.getError();
              ErrorCode errorCode = ErrorCode.UNEXPECTED;
              String errorMessage = apiError.getDescription();
              switch (apiError.getCode()) {
                case INVALID_PIN:
                  errorCode = ErrorCode.INCORRECT_PIN;
                  errorMessage = stringHelper.resolve(R.string.error_incorrect_pin);
                  break;
              }
              result = Result.create(FailureData.create(errorCode, errorMessage));
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
            view.startLoading();
          }
        })
        .subscribe(new Consumer<Result<Pair<UserData, String>, ErrorCode>>() {
          @Override
          public void accept(Result<Pair<UserData, String>, ErrorCode> result) throws Exception {
            view.stopLoading();
            if (result.isSuccessful()) {
              final Pair<UserData, String> successData = result.getSuccessData();
              userStore.set(successData.first);
              sessionBuilder.setToken(successData.second);
              view.moveToNextScreen();
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
            Timber.e(throwable, "Registering an user");
            view.stopLoading();
            view.showDialog(
              R.string.error_generic_title,
              R.string.error_generic,
              R.string.error_positive_button_text);
          }
        });
    }
  }

  final void onDigitButtonClicked(@Digit int digit) {
    builder.addDigit(digit);
    updateView();
  }

  final void onDeleteButtonClicked() {
    builder.removeLastDigit();
    updateView();
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
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

    void setTextInputContent(String content);

    void startLoading();

    void stopLoading();

    void moveToNextScreen();

    void showGenericErrorDialog(String message);
    void showGenericErrorDialog();
    void showUnavailableNetworkError();
  }
}
