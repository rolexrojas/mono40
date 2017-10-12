package com.tpago.movil.session;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.api.Api;
import com.tpago.movil.user.User;
import com.tpago.movil.user.UserManager;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public final class SessionManager {

  static Builder builder() {
    return new Builder();
  }

  private final Api api;
  private final AccessTokenManager accessTokenManager;
  private final UserManager userManager;

  private final List<Action> closeActionList;
  private final List<Action> clearActionList;

  private SessionManager(Builder builder) {
    this.api = builder.api;
    this.accessTokenManager = builder.accessTokenManager;
    this.userManager = builder.userManager;

    this.closeActionList = new ArrayList<>();
    this.closeActionList.add(this.accessTokenManager::clear);
    this.closeActionList.addAll(builder.closeActionList);

    this.clearActionList = new ArrayList<>();
    this.clearActionList.add(this.userManager::clear);
    this.clearActionList.addAll(builder.clearActionList);
  }

  public final boolean isUserSet() {
    return this.userManager.isSet();
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

  private void handleInitSuccess(Result<User> result) {
    if (result.isSuccessful()) {
      this.userManager.set(result.successData());
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

  public final User getUser() {
    return this.userManager.get();
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

  public final boolean isSessionOpen() {
    return this.accessTokenManager.isSet();
  }

  private Result<Placeholder> handleOpenSessionResult(Result<?> result) {
    final Result<Placeholder> placeholderResult;
    if (result.isSuccessful()) {
      placeholderResult = Result.create(Placeholder.get());
    } else {
      // TODO: Check if there is already a device associated, if true, reset/clear manager and notify observers.
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

  public final Completable closeSession() {
    this.checkUserIsSet();
    this.checkSessionIsOpen();
    return Observable.fromIterable(this.closeActionList)
      .flatMapCompletable(Completable::fromAction);
  }

  public final Completable clear() {
    return this.closeSession()
      .concatWith(
        Observable.fromIterable(this.clearActionList)
          .flatMapCompletable(Completable::fromAction)
      );
  }

  static final class Builder {

    private AccessTokenManager accessTokenManager;
    private Api api;
    private UserManager userManager;

    private List<Action> closeActionList;
    private List<Action> clearActionList;

    private Builder() {
      this.closeActionList = new ArrayList<>();
      this.clearActionList = new ArrayList<>();
    }

    final Builder accessTokenStore(AccessTokenManager accessTokenManager) {
      this.accessTokenManager = ObjectHelper.checkNotNull(accessTokenManager, "accessTokenManager");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final Builder userManager(UserManager userManager) {
      this.userManager = ObjectHelper.checkNotNull(userManager, "userManager");
      return this;
    }

    final Builder addCloseAction(Action closeAction) {
      ObjectHelper.checkNotNull(closeAction, "closeAction");
      if (!this.closeActionList.contains(closeAction)) {
        this.closeActionList.add(closeAction);
      }
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
        .addPropertyNameIfMissing(
          "accessTokenManager",
          ObjectHelper.isNull(this.accessTokenManager)
        )
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("userManager", ObjectHelper.isNull(this.userManager))
        .checkNoMissingProperties();

      return new SessionManager(this);
    }
  }
}
