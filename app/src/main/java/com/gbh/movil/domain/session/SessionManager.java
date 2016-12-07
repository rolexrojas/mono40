package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.recipient.RecipientService;
import com.gbh.movil.domain.text.PatternHelper;
import com.gbh.movil.domain.Pin;
import com.gbh.movil.misc.Utils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SessionManager {
  private final DeviceManager deviceManager;
  private final RecipientService recipientService;
  private final SessionRepo sessionRepo;
  private final SessionService sessionService;

  /**
   * TODO
   *
   * @param sessionRepo
   *   TODO
   */
  public SessionManager(@NonNull DeviceManager deviceManager,
    @NonNull RecipientService recipientService, @NonNull SessionRepo sessionRepo,
    @NonNull SessionService sessionService) {
    this.deviceManager = deviceManager;
    this.recipientService = recipientService;
    this.sessionRepo = sessionRepo;
    this.sessionService = sessionService;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  private Observable.Transformer<ApiResult<String>, Session> sign() {
    return new Observable.Transformer<ApiResult<String>, Session>() {
      @Override
      public Observable<Session> call(final Observable<ApiResult<String>> observable) {
        if (sessionRepo.hasSession()) {
          return Observable.just(null);
        } else {
          return observable
            .map(new Func1<ApiResult<String>, String>() {
              @Override
              public String call(ApiResult<String> result) {
                return result.isSuccessful() ? result.getData() : null;
              }
            })
            .flatMap(new Func1<String, Observable<Session>>() {
              @Override
              public Observable<Session> call(String token) {
                return Observable.zip(Observable.just(token),
                  recipientService.getName(token, deviceManager.getPhoneNumber()),
                  new Func2<String, ApiResult<String>, Session>() {
                    @Override
                    public Session call(String token, ApiResult<String> result) {
                      return result.isSuccessful() ? new Session(token, result.getData()) : null;
                    }
                  });
              }
            })
            .doOnNext(new Action1<Session>() {
              @Override
              public void call(Session session) {
                if (Utils.isNotNull(session)) {
                  sessionRepo.setSession(session);
                }
              }
            });
        }
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
  public final Observable<Session> signIn(@NonNull String email, @NonNull String password) {
    if (!PatternHelper.isValidEmail(email) && !PatternHelper.isValidPassword(password)) {
      return Observable.just(null);
    } else {
      return sessionService.signIn(deviceManager.getPhoneNumber(), email, password,
        deviceManager.getId())
        .compose(sign());
    }
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
  public final Observable<Session> signUp(@NonNull String email, @NonNull String password,
    @NonNull String pin) {
    if (!PatternHelper.isValidEmail(email) && !PatternHelper.isValidPassword(password)
      && !Pin.isValid(pin)) {
      return Observable.just(null);
    } else {
      return sessionService.signUp(deviceManager.getPhoneNumber(), email, password,
        deviceManager.getId(), pin)
        .compose(sign());
    }
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

  /**
   * TODO
   */
  public final void deactivate() {
    sessionRepo.clearSession();
  }
}
