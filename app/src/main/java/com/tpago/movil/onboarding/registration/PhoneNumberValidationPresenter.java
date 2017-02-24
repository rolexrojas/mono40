package com.tpago.movil.onboarding.registration;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiBridge;
import com.tpago.movil.api.ApiData;
import com.tpago.movil.api.ApiError;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class PhoneNumberValidationPresenter {
  private final ApiBridge apiBridge;
  private final RegistrationData data;
  private final StringResolver stringResolver;

  private View view;

  private boolean isPhoneNumberValid = false;
  private List<Digit> phoneNumberDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  PhoneNumberValidationPresenter(
    ApiBridge apiBridge,
    RegistrationData data,
    StringResolver stringResolver) {
    this.apiBridge = Preconditions.checkNotNull(apiBridge, "apiBridge == null");
    this.data = Preconditions.checkNotNull(data, "data == null");
    this.stringResolver = Preconditions.checkNotNull(stringResolver, "stringResolver == null");
  }

  private void updateView() {
    final String phoneNumber = Digits.stringify(phoneNumberDigits);
    isPhoneNumberValid = PhoneNumber.isValid(phoneNumber);
    if (Objects.isNotNull(view)) {
      view.setTextInputContent(phoneNumber);
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

  final void setView(View view) {
    this.view = view;
    final PhoneNumber phoneNumber = this.data.getPhoneNumber();
    if (Objects.isNotNull(phoneNumber)) {
      this.phoneNumberDigits.addAll(Digits.getDigits(phoneNumber));
    } else {
      Disposables.dispose(this.disposable);
    }
    this.updateView();
  }

  final void addDigit(Digit digit) {
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
      final PhoneNumber phoneNumber = PhoneNumber.create(Digits.stringify(phoneNumberDigits));
      disposable = apiBridge.validatePhoneNumber(phoneNumber)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            startLoading();
          }
        })
        .subscribe(new Consumer<HttpResult<ApiData<PhoneNumber.State>>>() {
          @Override
          public void accept(HttpResult<ApiData<PhoneNumber.State>> result) throws Exception {
            Timber.d(result.toString());
            stopLoading();
            final ApiData<PhoneNumber.State> apiData = result.getData();
            if (result.isSuccessful()) {
              data.setPhoneNumber(phoneNumber, apiData.getValue());
              view.moveToNextScreen();
            } else {
              final ApiError error = apiData.getError();
              view.showDialog(
                stringResolver.resolve(R.string.error_title),
                error.getDescription(),
                stringResolver.resolve(R.string.error_positive_button_text));
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Validating phone number");
            stopLoading();
            view.showDialog(
              stringResolver.resolve(R.string.error_title),
              stringResolver.resolve(R.string.error_message),
              stringResolver.resolve(R.string.error_positive_button_text));
          }
        });
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.phone_number_validation_error_incorrect_number_title),
        stringResolver.resolve(R.string.phone_number_validation_error_incorrect_number_message),
        stringResolver.resolve(R.string.phone_number_validation_error_incorrect_number_positive_button_text));
      view.showTextInputAsErratic(true);
    }
  }

  public interface View {
    void showDialog(String title, String message, String positiveButtonText);

    void setTextInputContent(String text);

    void showTextInputAsErratic(boolean showAsErratic);

    void setNextButtonEnabled(boolean enabled);

    void showNextButtonAsEnabled(boolean showAsEnabled);

    void startLoading();

    void stopLoading();

    void moveToNextScreen();
  }
}
