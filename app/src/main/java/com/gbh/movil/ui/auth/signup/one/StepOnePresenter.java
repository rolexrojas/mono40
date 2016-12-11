package com.gbh.movil.ui.auth.signup.one;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.text.PatternHelper;
import com.gbh.movil.domain.text.TextHelper;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.misc.rx.operators.WaitUntilOperator;
import com.gbh.movil.ui.Presenter;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class StepOnePresenter extends Presenter<StepOneScreen> {
  private final StringHelper stringHelper;

  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param stringHelper
   *   TODO
   */
  StepOnePresenter(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  /**
   * TODO
   */
  final void start() {
    subscription = Observable.combineLatest(
      screen.phoneNumberChanges(),
      screen.emailChanges(),
      screen.emailConfirmationChanges(),
      new Func3<String, String, String, InputData>() {
        @Override
        public InputData call(String phoneNumber, String email, String confirmation) {
          return new InputData(stringHelper, phoneNumber, email, confirmation);
        }
      })
      .subscribeOn(AndroidSchedulers.mainThread())
      .doOnNext(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          Timber.d(data.toString());
          screen.setPhoneNumberError(data.phoneNumberError);
          screen.setEmailError(data.emailError);
          screen.setConfirmationError(data.confirmationError);
          screen.setSubmitButtonEnabled(data.isValid());
        }
      })
      .lift(new WaitUntilOperator<InputData, Void>(screen.submitButtonClicks()))
      .subscribe(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          screen.submit(data.phoneNumber, data.email);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Signing up");
          // TODO: Let the user know that something failed.
        }
      });
  }

  /**
   * TODO
   */
  final void stop() {
    RxUtils.unsubscribe(subscription);
  }

  private static final class InputData {
    final String phoneNumber;
    final String phoneNumberError;
    final String email;
    final String emailError;
    final String confirmation;
    final String confirmationError;

    InputData(@NonNull StringHelper stringHelper, @Nullable String phoneNumber,
      @Nullable String email, @Nullable String confirmation) {
      if (TextHelper.isEmpty(phoneNumber)) {
        this.phoneNumberError = stringHelper.isRequired();
      } else if (!PhoneNumber.isValid(phoneNumber)) {
        this.phoneNumberError = stringHelper.incorrectFormat();
      } else {
        this.phoneNumberError = null;
      }
      this.phoneNumber = phoneNumber;
      if (TextHelper.isEmpty(email)) {
        this.emailError = stringHelper.isRequired();
      } else if (!PatternHelper.isValidEmail(email)) {
        this.emailError = stringHelper.incorrectFormat();
      } else {
        this.emailError = null;
      }
      this.email = email;
      if (TextHelper.isEmpty(confirmation)) {
        this.confirmationError = stringHelper.isRequired();
      } else if (!confirmation.equals(this.email)) {
        this.confirmationError = stringHelper.doesNotMatch();
      } else {
        this.confirmationError = null;
      }
      this.confirmation = confirmation;
    }

    final boolean isValid() {
      return Utils.isNull(phoneNumberError) && Utils.isNull(emailError)
        && Utils.isNull(confirmationError);
    }

    @Override
    public String toString() {
      return String.format("%1$s:{phoneNumber='%2$s',phoneNumberError='%3$s',email='%4$s'"
          + ",emailError='%5$s',confirmation='%6$s',confirmationError='%7$s'",
        InputData.class.getSimpleName(), phoneNumber, phoneNumberError, email, emailError,
        confirmation, confirmationError);
    }
  }
}
