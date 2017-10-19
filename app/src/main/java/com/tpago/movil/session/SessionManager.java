package com.tpago.movil.session;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.api.Api;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.store.Store;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.io.File;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class SessionManager {

  private static final String STORE_KEY_USER = "SessionManager.User";

  static Builder builder() {
    return new Builder();
  }

  private final AccessTokenStore accessTokenStore;
  private final Api api;
  private final JobManager jobManager;
  private final Store store;

  private final AtomicReference<User> userReference = new AtomicReference<>();
  private Disposable userDisposable = Disposables.disposed();

  private final List<Action> closeActions;
  private final List<Action> destroyActions;

  private CompositeDisposable compositeDisposable;

  private SessionManager(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.api = builder.api;
    this.jobManager = builder.jobManager;
    this.store = builder.store;

    this.closeActions = builder.closeActions;
    this.destroyActions = builder.destroyActions;
  }

  public final boolean isUserSet() {
    return this.store.isSet(STORE_KEY_USER);
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

  private void openSession() {
    DisposableHelper.dispose(this.userDisposable);
    this.userDisposable = this.userReference.get()
      .changes()
      .subscribeOn(Schedulers.io())
      .subscribe((user) -> this.store.set(STORE_KEY_USER, user));
  }

  private void handleCreateSessionSuccess(
    Result<User> result,
    boolean signedUp,
    @Nullable File picture
  ) {
    if (result.isSuccessful()) {
      final User user = result.successData();

      this.userReference.set(user);
      this.store.set(STORE_KEY_USER, user);

      this.openSession();

      if (signedUp) {
        this.jobManager.addJobInBackground(
          UpdateUserNameJob.create(user.firstName(), user.lastName())
        );
      } else {
        // TODO: Fetch carrier.
      }

      if (ObjectHelper.isNotNull(picture)) {
        this.jobManager.addJobInBackground(UpdateUserPictureJob.create(picture));
      }
    }
  }

  private void handleCreateSessionSuccess(Result<User> result) {
    this.handleCreateSessionSuccess(result, false, null);
  }

  public final Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    @Nullable File picture,
    Password password,
    Code pin,
    String deviceId
  ) {
    this.checkUserIsNotSet();

    return this.api.createSession(phoneNumber, email, firstName, lastName, password, pin, deviceId)
      .doOnSuccess((result) -> this.handleCreateSessionSuccess(result, true, picture));
  }

  public final Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    return this.createSession(
      phoneNumber,
      email,
      firstName,
      lastName,
      null,
      password,
      pin,
      deviceId
    );
  }

  public final Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    this.checkUserIsNotSet();

    return this.api.createSession(phoneNumber, email, password, deviceId, shouldDeactivatePreviousDevice)
      .doOnSuccess(this::handleCreateSessionSuccess);
  }

  public final User getUser() {
    this.checkUserIsSet();

    return this.userReference.get();
  }

  public final boolean isSessionOpen() {
    return this.accessTokenStore.isSet();
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

  private Result<Placeholder> mapOpenSessionResult(Result<?> result) {
    final Result<Placeholder> placeholderResult;
    if (result.isSuccessful()) {
      placeholderResult = Result.create(Placeholder.get());
    } else {
      placeholderResult = Result.create(result.failureData());
    }
    return placeholderResult;
  }

  private void handleOpenSessionResult(Result<Placeholder> result) {
    if (result.isSuccessful()) {
      this.openSession();
    }
  }

  public final Single<Result<Placeholder>> openSession(Password password, String deviceId) {
    this.checkUserIsSet();
    this.checkSessionIsNotOpen();

    final User user = this.getUser();
    return this.api.createSession(user.phoneNumber(), user.email(), password, deviceId, false)
      .map(this::mapOpenSessionResult)
      .doOnSuccess(this::handleOpenSessionResult);
  }

  public final Single<Result<Placeholder>> openSession(Signature signature, String deviceId) {
    this.checkUserIsSet();
    this.checkSessionIsNotOpen();

    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  public final void updateName(String firstName, String lastName) {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    this.jobManager.addJobInBackground(UpdateUserNameJob.create(firstName, lastName));
  }

  public final void updatePicture(File picture) {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    this.jobManager.addJobInBackground(UpdateUserPictureJob.create(picture));
  }

  public final void updateCarrier(Carrier carrier) {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    this.jobManager.addJobInBackground(UpdateUserCarrierJob.create(carrier));
  }

  private void closeSession_() {
    // TODO: Suspend all jobs instead of cancelling them.
//    this.jobManager.cancelJobs(TagConstraint.ANY, SessionJob.TAG);

    DisposableHelper.dispose(this.userDisposable);

    this.accessTokenStore.clear();
  }

  public final Completable closeSession() {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    Completable completable = Completable.fromAction(this::closeSession_);
    for (Action action : this.closeActions) {
      completable = completable.doOnComplete(action);
    }
    return completable;
  }

  private void destroySession_() {
    this.jobManager.cancelJobs(TagConstraint.ANY, SessionJob.TAG);

    this.compositeDisposable.dispose();
    this.compositeDisposable = null;

    this.store.remove(STORE_KEY_USER);
    this.userReference.set(null);
  }

  public final Completable destroySession() {
    this.checkUserIsSet();
    this.checkSessionIsOpen();

    Completable completable = this.closeSession()
      .concatWith(Completable.fromAction(this::destroySession_));
    for (Action action : this.destroyActions) {
      completable = completable.doOnComplete(action);
    }
    return completable;
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private Api api;
    private JobManager jobManager;
    private Store store;

    private final List<Action> closeActions;
    private final List<Action> destroyActions;

    private Builder() {
      this.closeActions = new ArrayList<>();
      this.destroyActions = new ArrayList<>();
    }

    final Builder accessTokenStore(AccessTokenStore accessTokenStore) {
      this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    final Builder jobManager(JobManager jobManager) {
      this.jobManager = ObjectHelper.checkNotNull(jobManager, "jobManager");
      return this;
    }

    final Builder store(Store store) {
      this.store = ObjectHelper.checkNotNull(store, "store");
      return this;
    }

    final Builder addCloseAction(Action action) {
      ObjectHelper.checkNotNull(action, "action");
      if (!this.closeActions.contains(action)) {
        this.closeActions.add(action);
      }
      return this;
    }

    final Builder addDestroyAction(Action action) {
      ObjectHelper.checkNotNull(action, "action");
      if (!this.destroyActions.contains(action)) {
        this.destroyActions.add(action);
      }
      return this;
    }

    final SessionManager build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("accessTokenStore", ObjectHelper.isNull(this.accessTokenStore))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("jobManager", ObjectHelper.isNull(this.jobManager))
        .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
        .checkNoMissingProperties();

      return new SessionManager(this);
    }
  }
}
