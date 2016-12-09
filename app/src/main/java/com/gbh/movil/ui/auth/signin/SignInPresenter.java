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
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.google.i18n.phonenumbers.NumberParseException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.functions.Func4;
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
  private final LoadIndicator loadIndicator;
  private final SessionManager sessionManager;

  /**
   * TODO
   */
  private Subscription validationSubscription = Subscriptions.unsubscribed();
  /**
   * TODO
   */
  private Subscription submissionSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param sessionManager
   *   TODO
   */
  SignInPresenter(@NonNull StringHelper stringHelper,
    @NonNull LoadIndicator loadIndicator, @NonNull SessionManager sessionManager) {
    this.stringHelper = stringHelper;
    this.loadIndicator = loadIndicator;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   */
  final void start() {
    final Observable<String> phoneNumberObservable = screen.afterPhoneNumberChanged();
    final Observable<String> emailObservable = screen.afterEmailChanged();
    final Observable<String> passwordObservable = screen.afterPasswordChanged();
    validationSubscription = Observable.combineLatest(phoneNumberObservable, emailObservable,
      passwordObservable, new Func3<String, String, String, Boolean>() {
        @Override
        public Boolean call(String phoneNumber, String email, String password) {
          boolean flag;
          String error;
          if (TextHelper.isEmpty(phoneNumber)) {
            error = stringHelper.isRequired();
          } else if (!PhoneNumber.isValid(phoneNumber)) {
            error = stringHelper.incorrectFormat();
          } else {
            error = null;
          }
          flag = Utils.isNull(error);
          screen.setPhoneNumberError(error);
          if (TextHelper.isEmpty(email)) {
            error = stringHelper.isRequired();
          } else if (!PatternHelper.isValidEmail(email)) {
            error = stringHelper.incorrectFormat();
          } else {
            error = null;
          }
          flag &= Utils.isNull(error);
          screen.setEmailError(error);
          if (TextHelper.isEmpty(password)) {
            error = stringHelper.isRequired();
          } else {
            error = null;
          }
          flag &= Utils.isNull(error);
          screen.setPasswordError(error);
          return flag;
        }
      })
      .subscribeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean flag) {
          screen.setSignInButtonEnabled(flag);
        }
      });
    submissionSubscription = Observable.combineLatest(phoneNumberObservable, emailObservable,
      passwordObservable, screen.onSignInButtonClicked(),
      new Func4<String, String, String, Void, Observable<Session>>() {
        @Override
        public Observable<Session> call(String phoneNumber, String email, String password,
          Void notification) {
          try {
            return sessionManager.signIn(new PhoneNumber(phoneNumber), email, password);
          } catch (NumberParseException exception) { // Not supposed to happen.
            return Observable.error(exception);
          }
        }
      })
      .subscribeOn(AndroidSchedulers.mainThread())
      .doOnNext(new Action1<Observable<Session>>() {
        @Override
        public void call(Observable<Session> observable) {
          loadIndicator.show();
        }
      })
      .observeOn(Schedulers.io())
      .flatMap(new Func1<Observable<Session>, Observable<Session>>() {
        @Override
        public Observable<Session> call(Observable<Session> observable) {
          return observable;
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Session>() {
        @Override
        public void call(Session session) {
          loadIndicator.hide();
          Timber.d("Session -> %1$s", session);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Signing in an user");
          loadIndicator.hide();
        }
      });
  }

  /**
   * TODO
   */
  final void stop() {
    RxUtils.unsubscribe(submissionSubscription);
    RxUtils.unsubscribe(validationSubscription);
  }
}
