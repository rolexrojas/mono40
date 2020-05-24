package com.mono40.movil.session;

import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.mono40.movil.Code;
import com.mono40.movil.Email;
import com.mono40.movil.Name;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.api.Api;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.lib.Password;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.store.DiskStore;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.StringHelper;

import java.io.File;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class SessionManager {

    private static final String STORE_KEY_UUID = createStoreKey("UUID");

    private static String createStoreKey(String s) {
        return String.format("SessionManager.%1$s", s);
    }

    private static final String STORE_KEY_USER = createStoreKey("User");
    private static final String STORE_KEY_UNLOCK_METHOD = createStoreKey("UnlockMethod");
    private static final String STORE_KEY_CUSTOMER_SECRET_TOKEN = createStoreKey("CustomerSecretToken");
    private static final String STORE_KEY_CUSTOMER_SECRET_KEY = createStoreKey("CustomerSecretKey");

    static Builder builder() {
        return new Builder();
    }

    private final AccessTokenStore accessTokenStore;
    private final Api api;
    private final JobManager jobManager;
    private final DiskStore diskStore;
    private final UnlockMethodDisableActionFactory unlockMethodDisableActionFactory;

    private final AtomicReference<User> userReference;
    private Disposable userDisposable = Disposables.disposed();

    private final AtomicReference<UnlockMethod> unlockMethodReference;

    private final List<SessionCloseAction> closeActions;
    private final List<SessionDestroyAction> destroyActions;

    private SessionManager(Builder builder) {
        this.accessTokenStore = builder.accessTokenStore;
        this.api = builder.api;
        this.jobManager = builder.jobManager;
        this.diskStore = builder.diskStore;
        this.unlockMethodDisableActionFactory = builder.unlockMethodDisableActionFactory;

        this.userReference = new AtomicReference<>();
        this.unlockMethodReference = new AtomicReference<>();

        this.closeActions = builder.closeActions;
        this.destroyActions = builder.destroyActions;
    }

    public final boolean isUserSet() {
        return this.diskStore.isSet(STORE_KEY_USER);
    }

    public final boolean isCustomerSecretTokenSet() {
        return this.diskStore.isSet(STORE_KEY_CUSTOMER_SECRET_TOKEN);
    }

    private void checkUserIsSet() {
        if (!this.isUserSet()) {
            throw new IllegalStateException("!this.isUserSet() and it should");
        }
    }

    private void checkUserIsNotSet() {
        if (this.isUserSet()) {
           // throw new IllegalStateException("this.isUserSet() and it shouldn't");
        }
    }

    private void createSession_(Result<User> result, boolean signedUp, @Nullable File picture) {
        if (result.isSuccessful()) {
            this.diskStore.set(STORE_KEY_USER, result.successData())
                    .blockingAwait();
            final User user = this.getUser();
            if (signedUp) {
                this.jobManager.addJobInBackground(UpdateUserNameJob.create(user.name()));
            } else {
                // TODO: Fetch carrier.
            }
            if (ObjectHelper.isNotNull(picture)) {
                this.jobManager.addJobInBackground(UpdateUserPictureJob.create(picture));
            }
        }
    }

    public void clearUserPicture() {
        this.jobManager.addJobInBackground(UpdateUserPictureJob.create(null));
    }

    private void createSession_(Result<User> result) {
        this.createSession_(result, false, null);
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
                .doOnSuccess((result) -> this.createSession_(result, true, picture));
    }

    public final Single<Result<User>> createSession(
            PhoneNumber phoneNumber,
            Email email,
            Password password,
            String deviceId,
            boolean shouldDeactivatePreviousDevice
    ) {
        this.checkUserIsNotSet();
        return this.api.createSession(
                phoneNumber,
                email,
                password,
                deviceId,
                shouldDeactivatePreviousDevice
        )
                .doOnSuccess(this::createSession_);
    }

    public final User getUser() {
        this.checkUserIsSet();
        User user = this.userReference.get();
        if (ObjectHelper.isNull(user)) {
            user = this.diskStore.get(STORE_KEY_USER, User.class)
                    .blockingGet();
            this.userReference.set(user);
            this.userDisposable = user.changes()
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            (u) -> this.diskStore.set(STORE_KEY_USER, u)
                                    .blockingAwait()
                    );
        }
        return user;
    }

    public final boolean isSessionOpen() {
        return this.accessTokenStore.isSet();
    }

    private void checkSessionIsOpen() {
        if (!this.isSessionOpen()) {
//            throw new IllegalStateException("!this.isSessionOpen()");
        }
    }

    private void checkSessionIsNotOpen() {
        if (this.isSessionOpen()) {
//            throw new IllegalStateException("this.isSessionOpen()");
        }
    }

    private void handleOpenSessionResult(Result<User> result) {
        if (result.isSuccessful()) {
            final Integer resultUserId = result.successData()
                    .id();
            final User user = this.getUser();
            final Integer userId = user.id();
            final boolean shouldUpdateId = ObjectHelper.isNotNull(resultUserId)
                    && !resultUserId.equals(userId);
            if (shouldUpdateId) {
                user.updateId(resultUserId);
            }
        }
    }

    public final Single<Result<User>> openSession(Password password, String deviceId) {
        this.checkUserIsSet();
        this.checkSessionIsNotOpen();
        final User user = this.getUser();
        return this.api.createSession(user.phoneNumber(), user.email(), password, deviceId, false)
                .doOnSuccess(this::handleOpenSessionResult);
    }

    private Single<Result<User>> openSession(
            UnlockMethodSignatureData signatureData,
            Signature signature
    ) throws Exception {
        return Single.just(signatureData.sign(signature))
                .flatMap((signedData) -> this.api.openSession(signatureData, signedData));
    }

    public final Single<Result<User>> openSession(Signature signature, String deviceId) {
        this.checkUserIsSet();
        this.checkUnlockMethodIsEnabled();
        this.checkSessionIsNotOpen();
        ObjectHelper.checkNotNull(signature, "signature");
        StringHelper.checkIsNotNullNorEmpty(deviceId, "deviceId");
        final UnlockMethodSignatureData signatureData = UnlockMethodSignatureData.builder()
                .user(this.getUser())
                .deviceId(deviceId)
                .build();
        return Single.defer(() -> this.openSession(signatureData, signature))
                .doOnSuccess(this::handleOpenSessionResult);
    }

    public final void updateName(Name name) {
        this.checkUserIsSet();
        this.checkSessionIsOpen();
        this.jobManager.addJobInBackground(UpdateUserNameJob.create(name));
    }

    public final void updatePicture(File picture) {
        this.checkUserIsSet();
        this.checkSessionIsOpen();
        this.jobManager.addJobInBackground(UpdateUserPictureJob.create(picture));
    }

    public final void updateCarrier(Partner carrier) {
        this.checkUserIsSet();
        this.checkSessionIsOpen();
        this.jobManager.addJobInBackground(UpdateUserCarrierJob.create(carrier));
    }

    public final boolean isUnlockMethodEnabled() {
        return this.diskStore.isSet(STORE_KEY_UNLOCK_METHOD);
    }

    public final boolean isUnlockMethodEnabled(UnlockMethod method) {
        ObjectHelper.checkNotNull(method, "method");
        return this.isUnlockMethodEnabled() && method.equals(this.getUnlockMethod());
    }

    private void checkUnlockMethodIsEnabled() {
        if (!this.isUnlockMethodEnabled()) {
            throw new IllegalStateException("!this.isUnlockMethodEnabled()");
        }
    }

    private void enableUnlockMethod(UnlockMethod method) {
        this.diskStore.set(STORE_KEY_UNLOCK_METHOD, method)
                .blockingAwait();
    }

    public void saveUUID(String uuid) {
        this.diskStore.set(STORE_KEY_UUID, uuid).blockingAwait();
    }

    public String getUUID() {
        return this.diskStore.get(STORE_KEY_UUID, String.class).blockingGet();
    }

    public void saveCustomerSecretToken(String token) {
        this.diskStore.set(STORE_KEY_CUSTOMER_SECRET_TOKEN, token).blockingAwait();
    }

    public void saveCustomerSecretKey(String key) {
        this.diskStore.set(STORE_KEY_CUSTOMER_SECRET_KEY, key).blockingAwait();
    }

    public final Completable enableUnlockMethod(UnlockMethodKeyGenerator generator) {
        this.checkUserIsSet();
        this.checkSessionIsOpen();
        ObjectHelper.checkNotNull(generator, "generator");
        final UnlockMethod method = generator.method();
        final UnlockMethodDisableAction disableAction = this.unlockMethodDisableActionFactory
                .make(method);
        Completable completable = generator.generate()
                .flatMapCompletable(this.api::enableUnlockMethod)
                .doOnComplete(() -> this.enableUnlockMethod(method))
                .doOnError((throwable) -> disableAction.run());
        if (this.isUnlockMethodEnabled()) {
            completable = this.disableUnlockMethod()
                    .concatWith(completable);
        }
        return completable;
    }

    public final UnlockMethod getUnlockMethod() {
        this.checkUserIsSet();
        this.checkUnlockMethodIsEnabled();
        UnlockMethod unlockMethod = this.unlockMethodReference.get();
        if (ObjectHelper.isNull(unlockMethod)) {
            unlockMethod = this.diskStore.get(STORE_KEY_UNLOCK_METHOD, UnlockMethod.class)
                    .blockingGet();
            this.unlockMethodReference.set(unlockMethod);
        }
        return unlockMethod;
    }

    public String getCustomerSecretToken() {
        return this.diskStore.get(STORE_KEY_CUSTOMER_SECRET_TOKEN, String.class).blockingGet();
    }

    public String getCustomerSecretKey() {
        return this.diskStore.get(STORE_KEY_CUSTOMER_SECRET_KEY, String.class).blockingGet();
    }

    private void disableUnlockMethod_() {
        this.unlockMethodReference.set(null);
        this.diskStore.remove(STORE_KEY_UNLOCK_METHOD)
                .blockingAwait();
    }

    public final Completable disableUnlockMethod() {
        this.checkUserIsSet();
        this.checkUnlockMethodIsEnabled();
        this.checkSessionIsOpen();
        return this.api.disableUnlockMethod()
                .doOnComplete(this.unlockMethodDisableActionFactory.make(this.getUnlockMethod()))
                .doOnComplete(this::disableUnlockMethod_);
    }

    private Completable onRequestForgotPassword(String email) {
        return this.api.requestForgotPassword(email);
    }

    public final Completable requestForgotPassword(String email) {
        return onRequestForgotPassword(email);
    }

    private Completable onRequestEditProfile(String firstName, String lastName) {
        Name newName = Name.create(firstName, lastName);
        this.getUser().updateName(newName);
        return this.api.updateUserName(this.getUser(), newName);
    }

    public final Completable requestEditProfile(String firstName, String lastName) {
        return onRequestEditProfile(firstName, lastName);
    }

    public final Completable closeSession() {
        this.checkUserIsSet();
        this.checkSessionIsOpen();

        Completable completable = null;
        for (SessionCloseAction action : this.closeActions) {
            if (ObjectHelper.isNull(completable)) {
                completable = Completable.fromAction(action);
            } else {
                completable = completable.concatWith(Completable.fromAction(action));
            }
        }
        // TODO: Suspend jobs instead of cancelling them.
        completable = completable.concatWith(
                Completable.fromAction(() -> this.jobManager.cancelJobs(TagConstraint.ANY, SessionJob.TAG))
        );
        return completable.doOnComplete(this.accessTokenStore::clear);
    }

    private void destroySession_() {
        DisposableUtil.dispose(this.userDisposable);
        this.userReference.set(null);
        this.diskStore.remove(STORE_KEY_USER)
                .blockingAwait();
    }

    public void destroySessionOnSigninForgotPassword() {
        this.destroySession_();
    }

    public final Completable destroySession() {
        this.checkUserIsSet();
        this.checkSessionIsOpen();
        final User user = this.getUser();
        Completable completable = null;
        for (SessionDestroyAction action : this.destroyActions) {
            if (ObjectHelper.isNull(completable)) {
                completable = Completable.fromAction(() -> action.run(user));
            } else {
                completable = completable.concatWith(Completable.fromAction(() -> action.run(user)));
            }
        }
        completable = completable.concatWith(
                Completable.fromAction(() -> this.jobManager.cancelJobs(TagConstraint.ANY, SessionJob.TAG))
        );
        if (this.isUnlockMethodEnabled()) {
            completable = completable.concatWith(this.disableUnlockMethod());
        }
        // this.destroySession_();
        return completable.doOnComplete(this::destroySession_);
    }

    static final class Builder {

        private AccessTokenStore accessTokenStore;
        private Api api;
        private JobManager jobManager;
        private DiskStore diskStore;
        private UnlockMethodDisableActionFactory unlockMethodDisableActionFactory;

        private final List<SessionCloseAction> closeActions;
        private final List<SessionDestroyAction> destroyActions;

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

        final Builder store(DiskStore diskStore) {
            this.diskStore = ObjectHelper.checkNotNull(diskStore, "diskStore");
            return this;
        }

        final Builder unlockMethodDisableActionFactory(UnlockMethodDisableActionFactory factory) {
            this.unlockMethodDisableActionFactory = ObjectHelper.checkNotNull(factory, "factory");
            return this;
        }

        final Builder closeActions(List<SessionCloseAction> actions) {
            ObjectHelper.checkNotNull(actions, "actions");
            this.closeActions.clear();
            this.closeActions.addAll(actions);
            return this;
        }

        final Builder destroyActions(List<SessionDestroyAction> actions) {
            ObjectHelper.checkNotNull(actions, "actions");
            this.destroyActions.clear();
            this.destroyActions.addAll(actions);
            return this;
        }

        final SessionManager build() {
            BuilderChecker.create()
                    .addPropertyNameIfMissing("accessTokenStore", ObjectHelper.isNull(this.accessTokenStore))
                    .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
                    .addPropertyNameIfMissing("jobManager", ObjectHelper.isNull(this.jobManager))
                    .addPropertyNameIfMissing("diskStore", ObjectHelper.isNull(this.diskStore))
                    .addPropertyNameIfMissing(
                            "unlockMethodDisableActionFactory",
                            ObjectHelper.isNull(this.unlockMethodDisableActionFactory)
                    )
                    .checkNoMissingProperties();

            return new SessionManager(this);
        }
    }
}
