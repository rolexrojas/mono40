package com.tpago.movil.d.ui.auth.signin;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.domain.api.ApiError;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.session.Session;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.text.PatternHelper;
import com.tpago.movil.d.domain.text.TextHelper;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.MessageDispatcher;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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

  private final AtomicBoolean mustForce = new AtomicBoolean(false);

  private InputData submissionData;

  private Subscription inputDataSubscription = Subscriptions.unsubscribed();
  private Subscription submissionSubscription = Subscriptions.unsubscribed();

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
    inputDataSubscription = Observable.combineLatest(
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
      .subscribe(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          submissionData = data;
          screen.setPhoneNumberError(submissionData.phoneNumberError);
          screen.setEmailError(submissionData.emailError);
          screen.setPasswordError(submissionData.passwordError);
          screen.setSubmitButtonEnabled(submissionData.isValid());
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
    RxUtils.unsubscribe(inputDataSubscription);
  }

  final void setMustForce(boolean mustForce) {
    this.mustForce.set(mustForce);
  }

  final void submit() {
    if (submissionSubscription.isUnsubscribed()) {
      submissionSubscription = sessionManager.signIn(
        submissionData.phoneNumber,
        submissionData.email,
        submissionData.password,
        mustForce.get())
        .flatMap(new Func1<ApiResult<String>, Observable<ApiResult<String>>>() {
          @Override
          public Observable<ApiResult<String>> call(final ApiResult<String> result) {
            if (result.isSuccessful()) {
              return initialDataLoader.load()
                .map(new Func1<Object, ApiResult<String>>() {
                  @Override
                  public ApiResult<String> call(Object o) {
                    return result;
                  }
                });
            } else {
              return Observable.just(result);
            }
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            loadIndicator.show();
          }
        })
        .subscribe(new Action1<ApiResult<String>>() {
          @Override
          public void call(ApiResult<String> result) {
            loadIndicator.hide();
            if (result.isSuccessful()) {
              screen.submit();
            } else {
              final ApiError error = result.getError();
              if (error != null) {
                if (error.getCode().equals(ApiError.Code.ALREADY_ASSOCIATED_DEVICE)) {
                  screen.showAlreadyAssociatedDialog();
                } else {
                  messageDispatcher.dispatch(result.getError().getDescription());
                }
              } else {
                messageDispatcher.dispatch(stringHelper.cannotProcessYourRequestAtTheMoment());
              }
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
