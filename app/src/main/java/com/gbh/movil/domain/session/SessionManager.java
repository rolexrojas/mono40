package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.DeviceManager;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.Pin;
import com.gbh.movil.domain.api.ApiResult;
import com.gbh.movil.domain.text.PatternHelper;
import com.gbh.movil.domain.text.TextHelper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func0;
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
  public final Observable<Session> signIn(@NonNull final PhoneNumber phoneNumber,
    @NonNull final String email, @NonNull final String password) {
    return Observable.defer(new Func0<Observable<Session>>() {
      @Override
      public Observable<Session> call() {
        final Map<Object, String> errors = new HashMap<>();
        if (TextHelper.isEmpty(email)) {
          errors.put(email, "Is required"); // TODO: Retrieve messages using the StringFactory.
        }
        if (!PatternHelper.isValidEmail(email)) {
          errors.put(email, "Incorrect format"); // TODO: Retrieve messages using the StringFactory.
        }
        if (TextHelper.isEmpty(password)) {
          errors.put(password, "Is required"); // TODO: Retrieve messages using the StringFactory.
        }
        if (sessionRepo.hasSession()) {
          final Session session = sessionRepo.getSession();
          if (!phoneNumber.equals(session.getPhoneNumber())) {
            errors.put(phoneNumber, "Not a match"); // TODO: Retrieve messages using the StringFactory.
          }
          if (!email.equals(session.getEmail())) {
            errors.put(email, "Not a match"); // TODO: Retrieve messages using the StringFactory.
          }
        }
        if (!errors.isEmpty()) {
          return Observable.just(null); // TODO: Propagate errors to the caller.
        } else {
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
  public final Observable<Session> signUp(@NonNull final PhoneNumber phoneNumber,
    @NonNull final String email, @NonNull final String password, @NonNull final String pin) {
    return Observable.defer(new Func0<Observable<Session>>() {
      @Override
      public Observable<Session> call() {
        final Map<Object, String> errors = new HashMap<>();
        if (TextHelper.isEmpty(email)) {
          errors.put(email, "Is required"); // TODO: Retrieve messages using the StringFactory.
        }
        if (!PatternHelper.isValidEmail(email)) {
          errors.put(email, "Incorrect format"); // TODO: Retrieve messages using the StringFactory.
        }
        if (TextHelper.isEmpty(password)) {
          errors.put(password, "Is required"); // TODO: Retrieve messages using the StringFactory.
        }
        if (!PatternHelper.isValidPassword(password)) {
          errors.put(password, "Incorrect format"); // TODO: Retrieve messages using the StringFactory.
        }
        if (TextHelper.isEmpty(pin)) {
          errors.put(pin, "Is required"); // TODO: Retrieve messages using the StringFactory.
        }
        if (!Pin.isValid(pin)) {
          errors.put(pin, "Incorrect format"); // TODO: Retrieve messages using the StringFactory.
        }
        // TODO: Validate the existent of a session.
        if (!errors.isEmpty()) {
          return Observable.just(null); // TODO: Propagate errors to the caller.
        } else {
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
