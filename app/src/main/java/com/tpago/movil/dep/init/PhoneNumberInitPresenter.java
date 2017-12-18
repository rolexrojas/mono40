package com.tpago.movil.dep.init;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.util.Digit;
import com.tpago.movil.util.DigitHelper;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class PhoneNumberInitPresenter extends Presenter<PhoneNumberInitPresenter.View> {

  @Inject AlertManager alertManager;
  @Inject Api api;
  @Inject InitData initData;
  @Inject StringMapper stringMapper;
  @Inject TakeoverLoader takeoverLoader;

  private boolean isPhoneNumberValid = false;
  private List<Integer> phoneNumberDigits = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  PhoneNumberInitPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    ObjectHelper.checkNotNull(component, "component")
      .inject(this);
  }

  private void updateView() {
    final String phoneNumber = DigitHelper.toDigitString(this.phoneNumberDigits);
    this.isPhoneNumberValid = PhoneNumber.isValid(phoneNumber);
    if (ObjectHelper.isNotNull(this.view)) {
      this.view.setTextInputContent(PhoneNumber.format(phoneNumber));
      this.view.showNextButtonAsEnabled(this.isPhoneNumberValid);
      if (this.isPhoneNumberValid) {
        this.view.showTextInputAsErratic(false);
      }
    }
  }

  private void showTakeoverLoader(Disposable disposable) {
    this.view.setNextButtonEnabled(false);
    this.view.showNextButtonAsEnabled(false);

    this.takeoverLoader.show();
  }

  private void hideTakeoverLoader() {
    this.takeoverLoader.hide();

    this.view.setNextButtonEnabled(true);
    this.view.showNextButtonAsEnabled(true);
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

  private void handleSuccess(PhoneNumber phoneNumber, @PhoneNumber.State int state) {
    if (state == PhoneNumber.State.NONE) {
      final AlertData data = AlertData.builder(this.stringMapper)
        .title(R.string.init_phone_number_error_not_affiliated_title)
        .message(R.string.init_phone_number_error_not_affiliated_message)
        .positiveButtonText(R.string.init_phone_number_error_not_affiliated_positive_button_text)
        .build();
      this.alertManager.show(data);
    } else {
      this.initData.setPhoneNumber(phoneNumber, state);
      if (state == PhoneNumber.State.AFFILIATED) {
        this.view.moveToSignUpScreen();
      } else {
        this.view.moveToSignInScreen();
      }
    }
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Fetching phone number state");
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  final void validate() {
    if (this.isPhoneNumberValid) {
      final PhoneNumber phoneNumber = PhoneNumber
        .create(DigitHelper.toDigitString(this.phoneNumberDigits));
      this.disposable = this.api.fetchPhoneNumberState(phoneNumber)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this::showTakeoverLoader)
        .doFinally(this::hideTakeoverLoader)
        .subscribe((s) -> this.handleSuccess(phoneNumber, s), this::handleError);
    } else {
      final AlertData data = AlertData.builder(this.stringMapper)
        .title(R.string.init_phone_number_error_incorrect_number_title)
        .message(R.string.init_phone_number_error_incorrect_number_message)
        .positiveButtonText(R.string.init_phone_number_error_incorrect_number_positive_button_text)
        .build();
      this.alertManager.show(data);
      this.view.showTextInputAsErratic(true);
    }
  }

  @Override
  public void onViewStarted() {
    final PhoneNumber phoneNumber = this.initData.getPhoneNumber();
    if (ObjectHelper.isNotNull(phoneNumber)) {
      this.phoneNumberDigits.clear();
      this.phoneNumberDigits.addAll(DigitHelper.toDigitList(phoneNumber.value()));
    }
    this.updateView();
  }

  @Override
  public void onViewStopped() {
    DisposableHelper.dispose(this.disposable);
  }

  interface View extends Presenter.View {

    void setTextInputContent(String text);

    void showTextInputAsErratic(boolean showAsErratic);

    void setNextButtonEnabled(boolean enabled);

    void showNextButtonAsEnabled(boolean showAsEnabled);

    void moveToSignInScreen();

    void moveToSignUpScreen();
  }
}
