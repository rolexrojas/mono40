package com.tpago.movil.data.api;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.session.SessionOpeningMethodConfigData;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.session.AccessTokenStore;
import com.tpago.movil.session.SessionOpeningSignatureData;
import com.tpago.movil.session.User;
import com.tpago.movil.store.Store;
import com.tpago.movil.api.Api;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.io.File;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;

/**
 * @author hecvasro
 */
final class MockApi implements Api {

  private static final long DELAY_VALUE = 1L;
  private static final TimeUnit DELAY_UNIT = TimeUnit.SECONDS;

  private static CompletableTransformer completableDelayTransformer() {
    return (u) -> u.delay(DELAY_VALUE, DELAY_UNIT);
  }

  private static <T> SingleTransformer<T, T> singleDelayTransformer() {
    return (u) -> u.delay(DELAY_VALUE, DELAY_UNIT);
  }

  private static String createMockApiStoreKey(String suffix) {
    return String.format("MockApi.%1$s", suffix);
  }

  private static final String STORE_KEY_USER_ID = createMockApiStoreKey("UserId");
  private static final String STORE_KEY_USER_EMAIL_SET = createMockApiStoreKey("UserEmailSet");

  private static String createUserStoreKey(PhoneNumber phoneNumber) {
    return createMockApiStoreKey(String.format("User.%1$s", phoneNumber.value()));
  }

  private static String createUserPasswordStoreKey(PhoneNumber phoneNumber) {
    return createMockApiStoreKey(String.format("UserPassword.%1$s", phoneNumber.value()));
  }

  private static String createUserPinStoreKey(PhoneNumber phoneNumber) {
    return createMockApiStoreKey(String.format("UserPin.%1$s", phoneNumber.value()));
  }

  private static String createUserPublicKeyStoreKey(PhoneNumber phoneNumber) {
    return createMockApiStoreKey(String.format("UserPublicKey.%1$s", phoneNumber.value()));
  }

  static Builder builder() {
    return new Builder();
  }

  private final AccessTokenStore accessTokenStore;
  private final SessionOpeningMethodConfigData sessionOpeningMethodConfigData;
  private final Store store;

  private final AtomicInteger userId;
  private final UserEmailSet userEmailSet;

  private MockApi(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.sessionOpeningMethodConfigData = builder.sessionOpeningMethodConfigData;
    this.store = builder.store;

    if (this.store.isSet(STORE_KEY_USER_ID)) {
      this.userId = new AtomicInteger(this.store.get(STORE_KEY_USER_ID, Integer.class));
    } else {
      this.userId = new AtomicInteger(1);
      this.store.set(STORE_KEY_USER_ID, this.userId.get());
    }

    if (this.store.isSet(STORE_KEY_USER_EMAIL_SET)) {
      this.userEmailSet = this.store.get(STORE_KEY_USER_EMAIL_SET, UserEmailSet.class);
    } else {
      this.userEmailSet = UserEmailSet.create();
      this.store.set(STORE_KEY_USER_EMAIL_SET, this.userEmailSet);
    }
  }

  private void setAccessToken(Result<?> result, PhoneNumber phoneNumber) {
    if (result.isSuccessful()) {
      this.accessTokenStore.set(phoneNumber.value());
    }
  }

  @Nullable
  private PhoneNumber getAccessToken() {
    return this.accessTokenStore.isSet() ? PhoneNumber.create(this.accessTokenStore.get()) : null;
  }

  private Single<Result<User>> signUp_(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    final Result<User> result;

    final String userStoreKey = createUserStoreKey(phoneNumber);
    if (this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.ALREADY_ASSOCIATED_PROFILE)
        .build();
      result = Result.create(failureData);
    } else if (this.userEmailSet.contains(email)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.ALREADY_REGISTERED_EMAIL)
        .build();
      result = Result.create(failureData);
    } else {
      final int newUserId = this.userId.addAndGet(1);
      this.store.set(STORE_KEY_USER_ID, newUserId);

      this.userEmailSet.add(email);
      this.store.set(STORE_KEY_USER_EMAIL_SET, this.userEmailSet);

      final User user = User.builder()
        .id(newUserId)
        .phoneNumber(phoneNumber)
        .email(email)
        .firstName(firstName)
        .lastName(lastName)
        .build();
      this.store.set(userStoreKey, user);

      final String userPasswordStoreKey = createUserPasswordStoreKey(phoneNumber);
      this.store.set(userPasswordStoreKey, password);

      final String userPinStoreKey = createUserPinStoreKey(phoneNumber);
      this.store.set(userPinStoreKey, pin);

      result = Result.create(user);
    }
    return Single.just(result);
  }

  @Override
  public Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    return Single.defer(() -> this.signUp_(
      phoneNumber,
      email,
      firstName,
      lastName,
      password,
      pin,
      deviceId
    ))
      .doOnSuccess((result) -> this.setAccessToken(result, phoneNumber))
      .compose(singleDelayTransformer());
  }

  private Single<Result<User>> signIn_(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    final Result<User> result;

    final String userStoreKey = createUserStoreKey(phoneNumber);
    if (!this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNASSOCIATED_PHONE_NUMBER)
        .build();
      result = Result.create(failureData);
    } else {
      final User user = this.store.get(userStoreKey, User.class);
      final Email userEmail = user.email();
      final Password userPassword = this.store
        .get(createUserPasswordStoreKey(phoneNumber), Password.class);
      if (!email.equals(userEmail) && !password.equals(userPassword)) {
        final FailureData failureData = FailureData.builder()
          .code(FailureCode.INCORRECT_USERNAME_AND_PASSWORD)
          .build();
        result = Result.create(failureData);
      } else {
        result = Result.create(user);
      }
    }

    return Single.just(result);
  }

  @Override
  public Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    return Single.defer(() -> this.signIn_(
      phoneNumber,
      email,
      password,
      deviceId,
      shouldDeactivatePreviousDevice
    ))
      .doOnSuccess((result) -> this.setAccessToken(result, phoneNumber))
      .compose(singleDelayTransformer());
  }

  @Override
  public Result<Placeholder> updateUserName(User user, String firstName, String lastName) {
    final Result<Placeholder> result;

    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      result = Result.create(failureData);
    } else {
      this.store.set(userStoreKey, user);
      result = Result.create(Placeholder.get());
    }

    return result;
  }

  @Override
  public Result<Uri> updateUserPicture(User user, File picture) {
    final Result<Uri> result;

    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      result = Result.create(failureData);
    } else {
      this.store.set(userStoreKey, user);
      result = Result.create(Uri.fromFile(picture));
    }

    return result;
  }

  @Override
  public Result<Placeholder> updateUserCarrier(User user, Carrier carrier) {
    final Result<Placeholder> result;

    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      result = Result.create(failureData);
    } else {
      this.store.set(userStoreKey, user);
      result = Result.create(Placeholder.get());
    }

    return result;
  }

  private void enableAltOpenSessionMethod_(PublicKey publicKey) throws Exception {
    final PhoneNumber userPhoneNumber = this.getAccessToken();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    if (!this.store.isSet(userStoreKey)) {
      throw new IllegalStateException("Unauthorized");
    } else {
      this.store.set(
        createUserPublicKeyStoreKey(userPhoneNumber),
        Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP)
      );
    }
  }

  @Override
  public Completable enableSessionOpeningMethod(PublicKey key) {
    return Completable.fromAction(() -> this.enableAltOpenSessionMethod_(key))
      .compose(completableDelayTransformer());
  }

  private Single<Result<Placeholder>> openSession_(
    SessionOpeningSignatureData signatureData,
    byte[] signedData
  ) throws Exception {
    final Result<Placeholder> result;

    final User user = signatureData.user();
    final PhoneNumber userPhoneNumber = user.phoneNumber();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    final String userPublicKeyStoreKey = createUserPublicKeyStoreKey(userPhoneNumber);
    if (!this.store.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      result = Result.create(failureData);
    } else if (!this.store.isSet(userPublicKeyStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNEXPECTED)
        .build();
      result = Result.create(failureData);
    } else {
      final String publicKeyBase64 = this.store.get(userPublicKeyStoreKey, String.class);
      final PublicKey publicKey = KeyFactory
        .getInstance(this.sessionOpeningMethodConfigData.keyGenAlgName())
        .generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyBase64, Base64.NO_WRAP)));
      final Signature signature = Signature.getInstance(this.sessionOpeningMethodConfigData.signAlgName());
      signature.initVerify(publicKey);
      if (!signatureData.verify(signature, signedData)) {
        final FailureData failureData = FailureData.builder()
          .code(FailureCode.UNEXPECTED)
          .build();
        result = Result.create(failureData);
      } else {
        result = Result.create(Placeholder.get());
      }
    }

    return Single.just(result);
  }

  @Override
  public Single<Result<Placeholder>> openSession(
    SessionOpeningSignatureData signatureData,
    byte[] signedData
  ) {
    return Single.defer(() -> this.openSession_(signatureData, signedData))
      .doOnSuccess(
        (result) -> this.setAccessToken(
          result,
          signatureData.user()
            .phoneNumber()
        )
      )
      .compose(singleDelayTransformer());
  }

  private void disableAltOpenSessionMethod_() throws Exception {
    final PhoneNumber userPhoneNumber = this.getAccessToken();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    if (!this.store.isSet(userStoreKey)) {
      throw new IllegalStateException("Unauthorized");
    } else {
      this.store.remove(createUserPublicKeyStoreKey(userPhoneNumber));
    }
  }

  @Override
  public Completable disableSessionOpeningMethod() {
    return Completable.fromAction(this::disableAltOpenSessionMethod_)
      .compose(completableDelayTransformer());
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private Store store;

    private SessionOpeningMethodConfigData sessionOpeningMethodConfigData;

    private Builder() {
    }

    final Builder accessTokenStore(AccessTokenStore accessTokenStore) {
      this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
      return this;
    }

    final Builder altAuthMethodConfigData(SessionOpeningMethodConfigData sessionOpeningMethodConfigData) {
      this.sessionOpeningMethodConfigData = ObjectHelper.checkNotNull(
        sessionOpeningMethodConfigData,
        "sessionOpeningMethodConfigData"
      );
      return this;
    }

    final Builder store(Store store) {
      this.store = ObjectHelper.checkNotNull(store, "store");
      return this;
    }

    final MockApi build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("accessTokenStore", ObjectHelper.isNull(this.accessTokenStore))
        .addPropertyNameIfMissing(
          "sessionOpeningMethodConfigData",
          ObjectHelper.isNull(this.sessionOpeningMethodConfigData)
        )
        .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
        .checkNoMissingProperties();

      return new MockApi(this);
    }
  }
}
