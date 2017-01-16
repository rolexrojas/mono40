package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.ResetEvent;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.util.EventType;
import com.gbh.movil.misc.rx.RxUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SessionManager {
  private static final long MAX_IDLE_TIME = 2L * 60L * 1000L; // Two (2) minutes.

  private final DeviceManager deviceManager;
  private final SessionRepo sessionRepo;
  private final SessionService sessionService;
  private final EventBus eventBus;

  private final Object expirationNotification = new Object();
  private final PublishSubject<Object> expirationSubject = PublishSubject.create();
  private Subscription expirationSubscription = Subscriptions.unsubscribed();
  private Subscription resettingSubscription = Subscriptions.unsubscribed();

  /**
   * TODO
   *
   * @param sessionRepo
   *   TODO
   */
  public SessionManager(@NonNull DeviceManager deviceManager, @NonNull SessionRepo sessionRepo,
    @NonNull SessionService sessionService, EventBus eventBus) {
    this.deviceManager = deviceManager;
    this.sessionRepo = sessionRepo;
    this.sessionService = sessionService;
    this.eventBus = eventBus;
  }

  private Func1<ApiResult<String>, AuthResult> authResultMapper(
    final String phoneNumber,
    final String email,
    final AuthCodeMapper codeMapper
  ) {
    return new Func1<ApiResult<String>, AuthResult>() {
      @Override
      public AuthResult call(ApiResult<String> result) {
        Session session = null;
        if (result.isSuccessful()) {
          session = new Session(phoneNumber, email, result.getData());
          sessionRepo.setSession(session);
          resettingSubscription = eventBus.onEventDispatched(EventType.SESSION_RESETTING)
            .subscribe(new Action1<Event>() {
              @Override
              public void call(Event event) {
                reset();
              }
            });
          eventBus.dispatch(new ResetEvent());
        }
        return new AuthResult(codeMapper.map(result.getCode()), session);
      }
    };
  }

  /**
   * TODO
   *
   * @param email
   *   TODO
   * @param password
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<AuthResult> signIn(
    final String phoneNumber,
    final String email,
    final String password,
    final boolean force) {
    return sessionService.signIn(phoneNumber, email, password, deviceManager.getId(), force)
      .map(authResultMapper(phoneNumber, email, new SignInCodeMapper()));
  }

  /**
   * TODO
   *
   * @param email
   *   TODO
   * @param password
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<AuthResult> signUp(final String phoneNumber, final String email,
    final String password, final String pin) {
    return sessionService.signUp(phoneNumber, email, password, deviceManager.getId(), pin)
      .map(authResultMapper(phoneNumber, email, new SignUpCodeMapper()));
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final Session getSession() {
    return isActive() ? sessionRepo.getSession() : null;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final boolean isActive() {
    return sessionRepo.hasSession();
  }

  public final Observable<Object> expiration() {
    return expirationSubject.asObservable();
  }

  public final void reset() {
    RxUtils.unsubscribe(expirationSubscription);
    expirationSubscription = Observable.just(expirationNotification)
      .delay(MAX_IDLE_TIME, TimeUnit.MILLISECONDS)
      .subscribe(new Action1<Object>() {
        @Override
        public void call(Object notification) {
          expirationSubject.onNext(notification);
        }
      });
  }

  /**
   * TODO
   */
  public final void deactivate() {
    RxUtils.unsubscribe(resettingSubscription);
    RxUtils.unsubscribe(expirationSubscription);
    sessionRepo.clearSession();
  }
}
