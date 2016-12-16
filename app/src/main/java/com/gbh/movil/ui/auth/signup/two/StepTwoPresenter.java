package com.gbh.movil.ui.auth.signup.two;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.session.Session;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.text.PatternHelper;
import com.gbh.movil.domain.text.TextHelper;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.MessageDispatcher;
import com.gbh.movil.ui.Presenter;
import com.gbh.movil.ui.view.widget.LoadIndicator;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
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
  private final MessageDispatcher messageDispatcher;
  private final LoadIndicator loadIndicator;
  private final SessionManager sessionManager;

  private final String phoneNumber;
  private final String email;

  private String password;

  private Subscription validationSubscription = Subscriptions.unsubscribed();
  private Subscription submissionSubscription = Subscriptions.unsubscribed();

  StepTwoPresenter(@NonNull StringHelper stringHelper, @NonNull MessageDispatcher messageDispatcher,
    @NonNull LoadIndicator loadIndicator, @NonNull SessionManager sessionManager,
    @NonNull String phoneNumber, @NonNull String email) {
    this.stringHelper = stringHelper;
    this.messageDispatcher = messageDispatcher;
    this.loadIndicator = loadIndicator;
    this.sessionManager = sessionManager;
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
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Action0() {
        @Override
        public void call() {
//          loadIndicator.show();
        }
      })
      .doOnUnsubscribe(new Action0() {
        @Override
        public void call() {
//          loadIndicator.hide();
        }
      })
      .subscribe(new Action1<Session>() {
        @Override
        public void call(Session session) {
          if (Utils.isNull(session)) {
            screen.submit(false);
          } else {
            screen.submit(true);
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
