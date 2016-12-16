package com.gbh.movil.ui.auth.signin;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.InitialDataLoader;
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
  private final InitialDataLoader initialDataLoader;

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
    @NonNull LoadIndicator loadIndicator, @NonNull SessionManager sessionManager,
    @NonNull InitialDataLoader initialDataLoader) {
    this.stringHelper = stringHelper;
    this.messageDispatcher = messageDispatcher;
    this.loadIndicator = loadIndicator;
    this.sessionManager = sessionManager;
    this.initialDataLoader = initialDataLoader;
  }

  /**
   * TODO
   */
  final void start() {
    subscription = Observable.combineLatest(
      screen.phoneNumberChanges(),
      screen.emailChanges(),
      screen.passwordChanges(),
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
          screen.setPhoneNumberError(data.phoneNumberError);
          screen.setEmailError(data.emailError);
          screen.setPasswordError(data.passwordError);
          screen.setSubmitButtonEnabled(data.isValid());
        }
      })
      .lift(new WaitUntilOperator<InputData, Void>(screen.submitButtonClicks()))
      .doOnNext(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          loadIndicator.show();
        }
      })
      .flatMap(new Func1<InputData, Observable<Session>>() {
        @Override
        public Observable<Session> call(InputData data) {
          return sessionManager.signIn(data.phoneNumber, data.email, data.password)
            .flatMap(new Func1<Session, Observable<Session>>() {
              @Override
              public Observable<Session> call(final Session session) {
                if (Utils.isNull(session)) {
                  return Observable.just(null);
                } else {
                  return initialDataLoader.load()
                    .map(new Func1<Object, Session>() {
                      @Override
                      public Session call(Object object) {
                        return session;
                      }
                    });
                }
              }
            })
            .subscribeOn(Schedulers.io());
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
            screen.submit();
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
    if (sessionManager.isActive()) {
      final Session session = sessionManager.getSession();
      if (Utils.isNotNull(session)) {
        screen.setPhoneNumber(session.getPhoneNumber());
        screen.setPhoneNumberEnabled(false);
        screen.setEmail(session.getEmail());
        screen.setEmailEnabled(false);
      }
    } else {
      screen.setPhoneNumberEnabled(true);
      screen.setEmailEnabled(true);
    }
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
    final String password;
    final String passwordError;

    InputData(@NonNull StringHelper stringHelper, String phoneNumber, String email,
      String password) {
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

    @Override
    public String toString() {
      return String.format("%1$s:{phoneNumber='%2$s',phoneNumberError='%3$s',email='%4$s',"
          + "emailError='%5$s',password='%6$s',passwordError='%7$s'}",
        InputData.class.getSimpleName(), phoneNumber, phoneNumberError, email, emailError, password,
        passwordError);
    }
  }
}
