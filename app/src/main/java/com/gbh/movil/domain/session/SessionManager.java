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

  /**
   * TODO
   */
  public final void deactivate() {
    sessionRepo.clearSession();
  }
}
