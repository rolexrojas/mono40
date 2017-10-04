package com.tpago.movil.dep.init;

import com.tpago.movil.util.Digit;
import com.tpago.movil.util.DigitHelper;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.dep.api.DApiBridge;
import com.tpago.movil.dep.api.DApiData;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.dep.net.HttpResult;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.dep.Objects;
import com.tpago.movil.dep.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.tpago.movil.util.DigitHelper.toDigitList;

/**
 * @author hecvasro
 */
public final class PhoneNumberInitPresenter extends Presenter<PhoneNumberInitPresenter.View> {
  @Inject
  DApiBridge apiBridge;
  @Inject
  InitData initData;
  @Inject
  NetworkService networkService;

  private boolean isPhoneNumberValid = false;
  private List<Integer> phoneNumberDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  PhoneNumberInitPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
  }

  private void updateView() {
    final String phoneNumber = DigitHelper.toDigitString(phoneNumberDigits);
    isPhoneNumberValid = PhoneNumber.isValid(phoneNumber);
    if (Objects.checkIfNotNull(view)) {
      view.setTextInputContent(PhoneNumber.format(phoneNumber));
      view.showNextButtonAsEnabled(isPhoneNumberValid);
      if (isPhoneNumberValid) {
        view.showTextInputAsErratic(false);
      }
    }
  }

  private void startLoading() {
    view.setNextButtonEnabled(false);
    view.showNextButtonAsEnabled(false);
    view.startLoading();
  }

  private void stopLoading() {
    view.stopLoading();
    view.setNextButtonEnabled(true);
    view.showNextButtonAsEnabled(true);
  }

  final void addDigit(@Digit int digit) {
    if (phoneNumberDigits.size() < 10) {
      phoneNumberDigits.add(digit);
      updateView();
    }
  }

  final void removeDigit() {
    if (!phoneNumberDigits.isEmpty()) {
      phoneNumberDigits.remove(phoneNumberDigits.size() - 1);
      updateView();
    }
  }

  final void validate() {
    if (isPhoneNumberValid) {
      final PhoneNumber phoneNumber = PhoneNumber.create(DigitHelper.toDigitString(phoneNumberDigits));
      disposable = Single
        .defer(new Callable<SingleSource<Result<Integer, ErrorCode>>>() {
          @Override
          public SingleSource<Result<Integer, ErrorCode>> call() throws Exception {
            final Result<Integer, ErrorCode> result;
            if (networkService.checkIfAvailable()) {
              final HttpResult<DApiData<Integer>> phoneNumberValidationResult = apiBridge
                .validatePhoneNumber(phoneNumber)
                .blockingGet();
              final DApiData<Integer> resultData = phoneNumberValidationResult.getData();
              if (phoneNumberValidationResult.isSuccessful()) {
                result = Result.create(resultData.getValue());
              } else {
                result = Result.create(
                  FailureData.create(
                    ErrorCode.UNEXPECTED,
                    resultData.getError().getDescription()));
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
        .subscribe(new Consumer<Result<Integer, ErrorCode>>() {
          @Override
          public void accept(Result<Integer, ErrorCode> result) throws Exception {
            stopLoading();
            if (result.isSuccessful()) {
              @PhoneNumber.State final int state = result.getSuccessData();
              if (state == PhoneNumber.State.NONE) {
                view.showDialog(
                  R.string.init_phone_number_error_not_affiliated_title,
                  R.string.init_phone_number_error_not_affiliated_message,
                  R.string.init_phone_number_error_not_affiliated_positive_button_text);
              } else {
                initData.setPhoneNumber(phoneNumber, state);
                if (state == PhoneNumber.State.REGISTERED) {
                  view.moveToSignInScreen();
                } else {
                  view.moveToSignUpScreen();
                }
              }
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
      view.showDialog(
        R.string.init_phone_number_error_incorrect_number_title,
        R.string.init_phone_number_error_incorrect_number_message,
        R.string.init_phone_number_error_incorrect_number_positive_button_text);
      view.showTextInputAsErratic(true);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final PhoneNumber phoneNumber = initData.getPhoneNumber();
    if (Objects.checkIfNotNull(phoneNumber)) {
      phoneNumberDigits.clear();
      phoneNumberDigits.addAll(toDigitList(phoneNumber.value()));
    }
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

    void setTextInputContent(String text);

    void showTextInputAsErratic(boolean showAsErratic);

    void setNextButtonEnabled(boolean enabled);

    void showNextButtonAsEnabled(boolean showAsEnabled);

    void startLoading();

    void stopLoading();

    void moveToSignInScreen();

    void moveToSignUpScreen();

    void showGenericErrorDialog(String message);
    void showGenericErrorDialog();
    void showUnavailableNetworkError();
  }
}
