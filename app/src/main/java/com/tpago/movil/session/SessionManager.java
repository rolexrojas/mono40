package com.tpago.movil.session;

import com.tpago.movil.Code;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

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

  private final List<Action> clearActionList;

  private SessionManager(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.api = builder.api;
    this.userStore = builder.userStore;

    this.clearActionList = builder.clearActionList;
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

  private void checkSessionIsNotOpen() {
    if (this.isSessionOpen()) {
      throw new IllegalStateException("this.isSessionOpen()");
    }
  }

  private void initUser(User user) {
    // TODO: Add name consumer that updates the API.
    // TODO: Add picture consumer that updates the API.
    // TODO: Add carrier consumer that updates the API.
  }

  private void setUser(User user) {
    this.userStore.set(user);
    this.initUser(user);
  }

  private void handleInitSuccess(Result<User> result) {
    if (result.isSuccessful()) {
      this.setUser(result.successData());
    }
  }

  public final Single<Result<User>> init(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    this.checkUserIsNotSet();

    return this.api.signUp(phoneNumber, email, firstName, lastName, password, pin, deviceId)
      .doOnSuccess(this::handleInitSuccess);
  }

  public final Single<Result<User>> init(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    this.checkUserIsNotSet();

    return this.api.signIn(phoneNumber, email, password, deviceId, shouldDeactivatePreviousDevice)
      .doOnSuccess(this::handleInitSuccess);
  }

  public final boolean isUserSet() {
    return this.userStore.isSet();
  }

  public final User getUser() {
    this.checkUserIsSet();

    return this.userStore.get();
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
    this.checkUserIsSet();
    this.checkSessionIsNotOpen();

    final User user = this.getUser();
    return this.api.signIn(user.phoneNumber(), user.email(), password, deviceId, false)
      .map(this::handleOpenSessionResult);
  }

  public final Single<Result<Placeholder>> openSession(PrivateKey privateKey, String deviceId) {
    this.checkUserIsSet();
    this.checkSessionIsNotOpen();

    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  public final boolean isSessionOpen() {
    return this.accessTokenStore.isSet();
  }

  public final void closeSession() {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    this.userStore.clear();
    this.accessTokenStore.clear();
  }

  public final Completable clear() {
    Completable completable = Completable.fromAction(this::closeSession);
    for (Action clearAction : this.clearActionList) {
      completable = completable.doOnComplete(clearAction);
    }
    return completable;
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private Api api;
    private UserStore userStore;

    private List<Action> clearActionList;

    private Builder() {
      this.clearActionList = new ArrayList<>();
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

    final Builder addClearAction(Action clearAction) {
      ObjectHelper.checkNotNull(clearAction, "clearAction");
      if (!this.clearActionList.contains(clearAction)) {
        this.clearActionList.add(clearAction);
      }
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
