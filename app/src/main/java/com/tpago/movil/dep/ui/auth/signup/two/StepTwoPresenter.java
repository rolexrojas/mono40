package com.tpago.movil.dep.ui.auth.signup.two;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.InitialDataLoader;
import com.tpago.movil.dep.domain.api.ApiError;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.text.PatternHelper;
import com.tpago.movil.dep.domain.text.TextHelper;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.MessageDispatcher;
import com.tpago.movil.dep.ui.Presenter;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class StepTwoPresenter extends Presenter<StepTwoScreen> {
  private final StringHelper stringHelper;
  private final SessionManager sessionManager;
  private final InitialDataLoader initialDataLoader;

  private final String phoneNumber;
  private final String email;

  private String password;

  private Subscription validationSubscription = Subscriptions.unsubscribed();
  private Subscription submissionSubscription = Subscriptions.unsubscribed();

  StepTwoPresenter(
    StringHelper stringHelper,
    MessageDispatcher messageDispatcher,
    SessionManager sessionManager,
    String phoneNumber,
    String email,
    InitialDataLoader initialDataLoader) {
    this.stringHelper = stringHelper;
    this.sessionManager = sessionManager;
    this.initialDataLoader = initialDataLoader;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }

  /**
   * TODO
   */
  final void start() {
    validationSubscription = Observable.combineLatest(
      screen.passwordChanges(),
      screen.passwordConfirmationChanges(),
      new Func2<String, String, InputData>() {
        @Override
        public InputData call(String password, String confirmation) {
          return new InputData(stringHelper, password, confirmation);
        }
      })
      .subscribeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<InputData>() {
        @Override
        public void call(InputData data) {
          password = data.password;
          screen.setPasswordError(data.passwordError);
          screen.setPasswordConfirmationError(data.confirmationError);
          screen.setSubmitButtonEnabled(data.isValid());
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Validating input data");
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

  final void onSubmitButtonClicked(@NonNull String pin) {
    RxUtils.unsubscribe(submissionSubscription);
    submissionSubscription = sessionManager.signUp(phoneNumber, email, password, pin)
      .subscribeOn(Schedulers.io())
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
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          if (result.isSuccessful()) {
            screen.submit(true);
          } else {
            final ApiError error = result.getError();
            if (error != null) {
              Timber.d(error.toString());
            }
            screen.submit(false);
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Signing up");
          screen.submit(false);
        }
      });
  }

  private static final class InputData {
    final String password;
    final String passwordError;
    final String confirmation;
    final String confirmationError;

    InputData(@NonNull StringHelper stringHelper, @Nullable String password,
      @Nullable String confirmation) {
      if (TextHelper.isEmpty(password)) {
        this.passwordError = stringHelper.isRequired();
      } else if (!PatternHelper.isValidPassword(password)) {
        this.passwordError = stringHelper.incorrectFormat();
      } else {
        this.passwordError = null;
      }
      this.password = password;
      if (TextHelper.isEmpty(confirmation)) {
        this.confirmationError = stringHelper.isRequired();
      } else if (!confirmation.equals(this.password)) {
        this.confirmationError = stringHelper.doesNotMatch();
      } else {
        this.confirmationError = null;
      }
      this.confirmation = confirmation;
    }

    final boolean isValid() {
      return Utils.isNull(passwordError) && Utils.isNull(confirmationError);
    }

    @Override
    public String toString() {
      return String.format("%1$s:{password='%2$s',passwordError='%3$s',confirmation='%4$s'" +
          ",confirmationError='%5$s'}", InputData.class.getSimpleName(), password, passwordError,
        confirmation, confirmationError);
    }
  }
}
