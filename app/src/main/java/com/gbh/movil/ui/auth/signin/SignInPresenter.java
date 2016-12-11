package com.gbh.movil.ui.auth.signin;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.session.Session;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.text.PatternHelper;
import com.gbh.movil.domain.text.TextHelper;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.misc.rx.operators.WaitUntilOperator;
import com.gbh.movil.ui.MessageDispatcher;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.google.i18n.phonenumbers.NumberParseException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class SignInPresenter extends Presenter<SignInScreen> {
  private final StringHelper stringHelper;
  private final MessageDispatcher messageDispatcher;
  private final LoadIndicator loadIndicator;
  private final SessionManager sessionManager;

  /**
   * TODO
   */
  private Subscription subscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param sessionManager
   *   TODO
   */
  SignInPresenter(@NonNull StringHelper stringHelper, @NonNull MessageDispatcher messageDispatcher,
    @NonNull LoadIndicator loadIndicator, @NonNull SessionManager sessionManager) {
    this.stringHelper = stringHelper;
    this.messageDispatcher = messageDispatcher;
    this.loadIndicator = loadIndicator;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   */
  final void start() {
    subscription = Observable.combineLatest(
      screen.afterPhoneNumberChanged(),
      screen.afterEmailChanged(),
      screen.afterPasswordChanged(),
      new Func3<String, String, String, InputData>() {
        @Override
        public InputData call(String phoneNumber, String email, String password) {
          return new InputData(stringHelper, phoneNumber, email, password);
        }
      })
      .subscribeOn(AndroidSchedulers.mainThread())
      .doOnNext(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          screen.setPhoneNumberError(data.getPhoneNumberError());
          screen.setEmailError(data.getEmailError());
          screen.setPasswordError(data.getPasswordError());
          screen.setSignInButtonEnabled(data.isValid());
        }
      })
      .doOnNext(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          Timber.d(data.toString());
        }
      })
      .lift(new WaitUntilOperator<InputData, Void>(screen.onSignInButtonClicked()))
      .doOnNext(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          loadIndicator.show();
        }
      })
      .observeOn(Schedulers.io())
      .flatMap(new Func1<InputData, Observable<Session>>() {
        @Override
        public Observable<Session> call(InputData data) {
          return sessionManager.signIn(data.getPhoneNumber(), data.getEmail(), data.getPassword());
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Session>() {
        @Override
        public void call(Session session) {
          loadIndicator.hide();
          if (Utils.isNull(session)) {
            messageDispatcher.dispatch(stringHelper.cannotProcessYourRequestAtTheMoment());
          } else {
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Signing in");
          loadIndicator.hide();
          messageDispatcher.dispatch(stringHelper.cannotProcessYourRequestAtTheMoment());
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
    private PhoneNumber phoneNumber;
    private final String phoneNumberError;
    private final String email;
    private final String emailError;
    private final String password;
    private final String passwordError;

    InputData(@NonNull StringHelper stringHelper, String phoneNumber, String email,
      String password) {
      if (TextHelper.isEmpty(phoneNumber)) {
        this.phoneNumberError = stringHelper.isRequired();
      } else if (!PhoneNumber.isValid(phoneNumber)) {
        this.phoneNumberError = stringHelper.incorrectFormat();
      } else {
        this.phoneNumberError = null;
      }
      if (Utils.isNull(phoneNumberError)) {
        try {
          this.phoneNumber = new PhoneNumber(phoneNumber);
        } catch (NumberParseException exception) {
          this.phoneNumber = null;
        }
      } else {
        this.phoneNumber = null;
      }
      if (TextHelper.isEmpty(email)) {
        this.emailError = stringHelper.isRequired();
      } else if (!PatternHelper.isValidEmail(email)) {
        this.emailError = stringHelper.incorrectFormat();
      } else {
        this.emailError = null;
      }
      this.email = email;
      if (TextHelper.isEmpty(password)) {
        this.passwordError = stringHelper.isRequired();
      } else {
        this.passwordError = null;
      }
      this.password = password;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    final boolean isValid() {
      return Utils.isNull(phoneNumberError) && Utils.isNull(emailError)
        && Utils.isNull(passwordError);
    }

    final PhoneNumber getPhoneNumber() {
      return phoneNumber;
    }

    final String getPhoneNumberError() {
      return phoneNumberError;
    }

    final String getEmail() {
      return email;
    }

    final String getEmailError() {
      return emailError;
    }

    final String getPassword() {
      return password;
    }

    final String getPasswordError() {
      return passwordError;
    }

    @Override
    public String toString() {
      return String.format("%1$s:{phoneNumber='%2$s',phoneNumberError='%3$s',email='%4$s',"
          + "emailError='%5$s',password='%6$s',passwordError='%7$s'", InputData.class.getSimpleName(),
        phoneNumber, phoneNumberError, email, emailError, password, passwordError);
    }
  }
}
