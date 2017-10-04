package com.tpago.movil.session;

import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.api.Api;
import com.tpago.movil.user.User;
import com.tpago.movil.user.UserStore;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PrivateKey;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class SessionManager {

  static Builder builder() {
    return new Builder();
  }

  private final AccessTokenStore accessTokenStore;
  private final Api api;
  private final UserStore userStore;

  private AtomicReference<User> userReference;

  private SessionManager(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.api = builder.api;
    this.userStore = builder.userStore;
  }

  private void checkUserIsSet() {
    if (!this.isUserSet()) {
      throw new IllegalStateException("!this.isUserSet()");
    }
  }

  private void checkUserIsNotSet() {
    if (this.isUserSet()) {
      throw new IllegalStateException("this.isUserSet()");
    }
  }

  private void checkSessionIsOpen() {
    if (!this.isSessionOpen()) {
      throw new IllegalStateException("!this.isSessionOpen()");
    }
  }

  private void setUserReference(User user) {
    // TODO: Add name consumer that updates the API.
    // TODO: Add picture consumer that updates the API.
    // TODO: Add carrier consumer that updates the API.

    this.userReference.set(user);
  }

  private void handleInitSuccess(Result<User> result) {
    if (result.isSuccessful()) {
      final User user = result.successData();
      this.userStore.set(user);
      this.setUserReference(user);
    }
  }

  public final Single<Result<User>> init(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    this.checkUserIsNotSet();

    ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");
    ObjectHelper.checkNotNull(email, "email");
    ObjectHelper.checkNotNull(password, "password");
    ObjectHelper.checkNotNull(deviceId, "deviceId");

    return this.api.signIn(phoneNumber, email, password, deviceId, shouldDeactivatePreviousDevice)
      .doOnSuccess(this::handleInitSuccess);
  }

  public final boolean isUserSet() {
    return this.userStore.isSet();
  }

  public final User getUser() {
    this.checkUserIsSet();

    User user = this.userReference.get();
    if (ObjectHelper.isNull(user)) {
      user = this.userStore.get();
      this.setUserReference(user);
    }
    return user;
  }

  private Result<Placeholder> handleOpenSessionResult(Result<?> result) {
    final Result<Placeholder> placeholderResult;
    if (result.isSuccessful()) {
      placeholderResult = Result.create(Placeholder.get());
    } else {
      // TODO: Check if there is already a device associated, if true, reset/clear manager and notify client.
      placeholderResult = Result.create(result.failureData());
    }
    return placeholderResult;
  }

  public final Single<Result<Placeholder>> openSession(Password password, String deviceId) {
    final User user = this.getUser();

    ObjectHelper.checkNotNull(password, "password");
    ObjectHelper.checkNotNull(deviceId, "deviceId");

    return this.api.signIn(user.phoneNumber(), user.email(), password, deviceId, false)
      .map(this::handleOpenSessionResult);
  }

  public final Single<Result<Placeholder>> openSession(
    Single<Result<PrivateKey>> privateKey,
    String deviceId
  ) {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  public final boolean isSessionOpen() {
    return this.accessTokenStore.isSet();
  }

  public final void closeSession() {
    this.checkSessionIsOpen();

    this.accessTokenStore.clear();
  }

  public final Completable reset() {
    return Completable.error(new UnsupportedOperationException("not implemented"));
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private Api api;
    private UserStore userStore;

    private Builder() {
    }

    final Builder accessTokenStore(AccessTokenStore accessTokenStore) {
      this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final Builder userStore(UserStore userStore) {
      this.userStore = ObjectHelper.checkNotNull(userStore, "userStore");
      return this;
    }

    final SessionManager build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("accessTokenStore", ObjectHelper.isNull(this.accessTokenStore))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("userStore", ObjectHelper.isNull(this.userStore))
        .checkNoMissingProperties();

      return new SessionManager(this);
    }
  }
}
