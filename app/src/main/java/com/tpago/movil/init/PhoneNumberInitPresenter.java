package com.tpago.movil.init;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.api.DApiBridge;
import com.tpago.movil.api.DApiData;
import com.tpago.movil.api.DApiError;
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
public final class PhoneNumberInitPresenter extends Presenter<PhoneNumberInitPresenter.View> {
  @Inject
  DApiBridge DApiBridge;
  @Inject InitData initData;

  private boolean isPhoneNumberValid = false;
  private List<Digit> phoneNumberDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  PhoneNumberInitPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
  }

  private void updateView() {
    final String phoneNumber = Digits.stringify(phoneNumberDigits);
    isPhoneNumberValid = PhoneNumber.checkIfValid(phoneNumber);
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
      disposable = DApiBridge.validatePhoneNumber(phoneNumber)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            startLoading();
          }
        })
        .subscribe(new Consumer<HttpResult<DApiData<PhoneNumber.State>>>() {
          @Override
          public void accept(HttpResult<DApiData<PhoneNumber.State>> result) throws Exception {
            stopLoading();
            final DApiData<PhoneNumber.State> DApiData = result.getData();
            if (result.isSuccessful()) {
              final PhoneNumber.State state = DApiData.getValue();
              if (state.equals(PhoneNumber.State.NONE)) {
                view.showDialog(
                  R.string.init_phone_number_error_not_affiliated_title,
                  R.string.init_phone_number_error_not_affiliated_message,
                  R.string.init_phone_number_error_not_affiliated_positive_button_text);
              } else {
                initData.setPhoneNumber(phoneNumber, state);
                if (state.equals(PhoneNumber.State.REGISTERED)) {
                  view.moveToSignInScreen();
                } else {
                  view.moveToSignUpScreen();
                }
              }
            } else {
              final DApiError error = DApiData.getError();
              view.showDialog(
                R.string.error_generic_title,
                error.getDescription(),
                R.string.error_positive_button_text);
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);
            stopLoading();
            view.showDialog(
              R.string.error_generic_title,
              R.string.error_generic,
              R.string.error_positive_button_text);
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
