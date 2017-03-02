package com.tpago.movil.init;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiBridge;
import com.tpago.movil.api.ApiData;
import com.tpago.movil.api.ApiError;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class PhoneNumberFormPresenter extends Presenter<PhoneNumberFormPresenter.View> {
  @Inject ApiBridge apiBridge;
  @Inject InitData initData;

  private boolean isPhoneNumberValid = false;
  private List<Digit> phoneNumberDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  PhoneNumberFormPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.checkNotNull(component, "component == null")
      .inject(this);
  }

  private void updateView() {
    final String phoneNumber = Digits.stringify(phoneNumberDigits);
    isPhoneNumberValid = PhoneNumber.isValid(phoneNumber);
    if (Objects.isNotNull(view)) {
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
            stopLoading();
            final ApiData<PhoneNumber.State> apiData = result.getData();
            if (result.isSuccessful()) {
              final PhoneNumber.State state = apiData.getValue();
              initData.setPhoneNumber(phoneNumber, state);
              if (state.equals(PhoneNumber.State.REGISTERED)) {
                view.moveToSignInScreen();
              } else {
                view.moveToSignUpScreen();
              }
            } else {
              final ApiError error = apiData.getError();
              view.showDialog(
                R.string.error_title,
                error.getDescription(),
                R.string.error_positive_button_text);
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Validating phone number");
            stopLoading();
            view.showDialog(
              R.string.error_title,
              R.string.error_message,
              R.string.error_positive_button_text);
          }
        });
    } else {
      view.showDialog(
        R.string.register_form_phone_number_error_incorrect_number_title,
        R.string.register_form_phone_number_error_incorrect_number_message,
        R.string.register_form_phone_number_error_incorrect_number_positive_button_text);
      view.showTextInputAsErratic(true);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final PhoneNumber phoneNumber = initData.getPhoneNumber();
    if (Objects.isNotNull(phoneNumber)) {
      phoneNumberDigits.addAll(Digits.getDigits(phoneNumber));
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
  }
}
