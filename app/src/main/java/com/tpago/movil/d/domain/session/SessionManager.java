package com.tpago.movil.d.domain.session;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.DepDeviceManager;
import com.tpago.movil.d.domain.ResetEvent;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.misc.rx.RxUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class SessionManager {
  private static final long MAX_IDLE_TIME = 1L * 30L * 1000L; // Two (2) minutes.

  private final DepDeviceManager deviceManager;
  private final SessionRepo sessionRepo;
  private final SessionService sessionService;
  private final EventBus eventBus;

  private final Object expirationNotification = new Object();
  private final PublishSubject<Object> expirationSubject = PublishSubject.create();
  private Subscription expirationSubscription = Subscriptions.unsubscribed();
  private Subscription resettingSubscription = Subscriptions.unsubscribed();

  public SessionManager(
    @NonNull DepDeviceManager deviceManager,
    @NonNull SessionRepo sessionRepo,
    @NonNull SessionService sessionService,
    EventBus eventBus) {
    this.deviceManager = deviceManager;
    this.sessionRepo = sessionRepo;
    this.sessionService = sessionService;
    this.eventBus = eventBus;
  }

  private Action1<ApiResult<String>> authAction1(final String phoneNumber, final String email) {
    return new Action1<ApiResult<String>>() {
      @Override
      public void call(ApiResult<String> result) {
        if (result.isSuccessful()) {
          final Session session = new Session(phoneNumber, email, result.getData());
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
      }
    };
  }

  @NonNull
  public final Observable<ApiResult<String>> signIn(
    final String phoneNumber,
    final String email,
    final String password,
    final boolean force) {
    return sessionService.signIn(phoneNumber, email, password, deviceManager.getId(), force)
      .doOnNext(authAction1(phoneNumber, email));
  }

  @NonNull
  public final Observable<ApiResult<String>> signUp(final String phoneNumber, final String email,
    final String password, final String pin) {
    return sessionService.signUp(phoneNumber, email, password, deviceManager.getId(), pin)
      .doOnNext(authAction1(phoneNumber, email));
  }

  public final Session getSession() {
    return isActive() ? sessionRepo.getSession() : null;
  }

  public final boolean isActive() {
    return sessionRepo.hasSession();
  }

  public final Observable<Object> expiration() {
    return expirationSubject.asObservable();
  }

  final void reset() {
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

  public final void deactivate() {
    RxUtils.unsubscribe(resettingSubscription);
    RxUtils.unsubscribe(expirationSubscription);
    sessionRepo.clearSession();
  }
}
