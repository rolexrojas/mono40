package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.api.ApiResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SessionManager {
  private final DeviceManager deviceManager;
  private final SessionRepo sessionRepo;
  private final SessionService sessionService;

  /**
   * TODO
   *
   * @param sessionRepo
   *   TODO
   */
  public SessionManager(@NonNull DeviceManager deviceManager, @NonNull SessionRepo sessionRepo,
    @NonNull SessionService sessionService) {
    this.deviceManager = deviceManager;
    this.sessionRepo = sessionRepo;
    this.sessionService = sessionService;
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
  public final Observable<Session> signIn(final String phoneNumber, final String email,
    final String password) {
    // TODO: Validate the phone number, email and password.
    return sessionService.signIn(phoneNumber, email, password, deviceManager.getId())
      .map(new Func1<ApiResult<String>, Session>() {
        @Override
        public Session call(ApiResult<String> result) {
          if (result.isSuccessful()) {
            final Session session = new Session(phoneNumber, email, result.getData());
            sessionRepo.setSession(session);
            return session;
          } else {
            return null; // TODO: Propagate errors to the caller.
          }
        }
      });
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
  public final Observable<Session> signUp(final String phoneNumber, final String email,
    final String password, final String pin) {
    // TODO: Validate the phone number, email, password and pin.
    return sessionService.signUp(phoneNumber, email, password, deviceManager.getId(), pin)
      .map(new Func1<ApiResult<String>, Session>() {
        @Override
        public Session call(ApiResult<String> result) {
          if (result.isSuccessful()) {
            final Session session = new Session(phoneNumber, email, result.getData());
            sessionRepo.setSession(session);
            return session;
          } else {
            return null; // TODO: Propagate errors to the caller.
          }
        }
      });
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
