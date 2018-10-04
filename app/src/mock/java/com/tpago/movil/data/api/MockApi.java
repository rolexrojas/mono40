package com.tpago.movil.data.api;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Name;
import com.tpago.movil.lib.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.io.FileHelper;
import com.tpago.movil.session.AccessTokenStore;
import com.tpago.movil.session.SessionData;
import com.tpago.movil.session.UnlockMethodConfigData;
import com.tpago.movil.session.UnlockMethodSignatureData;
import com.tpago.movil.session.User;
import com.tpago.movil.store.DiskStore;
import com.tpago.movil.api.Api;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
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
  private final Context context;
  private final UnlockMethodConfigData configData;
  private final DiskStore diskStore;

  private final AtomicInteger userId;
  private final UserEmailSet userEmailSet;

  private final MockData mockData;

  private MockApi(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.configData = builder.configData;
    this.context = builder.context;
    this.diskStore = builder.diskStore;

    this.mockData = builder.mockData;

    if (this.diskStore.isSet(STORE_KEY_USER_ID)) {
      this.userId = new AtomicInteger(this.diskStore.get(STORE_KEY_USER_ID, Integer.class));
    } else {
      this.userId = new AtomicInteger(1);
      this.diskStore.set(STORE_KEY_USER_ID, this.userId.get());
    }

    if (this.diskStore.isSet(STORE_KEY_USER_EMAIL_SET)) {
      this.userEmailSet = this.diskStore.get(STORE_KEY_USER_EMAIL_SET, UserEmailSet.class);
    } else {
      this.userEmailSet = UserEmailSet.create();
      this.diskStore.set(STORE_KEY_USER_EMAIL_SET, this.userEmailSet);
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
    if (this.diskStore.isSet(userStoreKey)) {
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
      this.diskStore.set(STORE_KEY_USER_ID, newUserId);

      this.userEmailSet.add(email);
      this.diskStore.set(STORE_KEY_USER_EMAIL_SET, this.userEmailSet);

      final User user = User.builder()
        .id(newUserId)
        .phoneNumber(phoneNumber)
        .email(email)
        .name(Name.create(firstName, lastName))
        .build();
      this.diskStore.set(userStoreKey, user);

      final String userPasswordStoreKey = createUserPasswordStoreKey(phoneNumber);
      this.diskStore.set(userPasswordStoreKey, password);

      final String userPinStoreKey = createUserPinStoreKey(phoneNumber);
      this.diskStore.set(userPinStoreKey, pin);

      result = Result.create(user);
    }
    return Single.just(result);
  }

  @Override
  public Single<Integer> fetchPhoneNumberState(PhoneNumber phoneNumber) {
    ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");
    return Single.defer(() -> {
      @PhoneNumber.State final int userState;
      final String userStoreKey = createUserStoreKey(phoneNumber);
      if (this.diskStore.isSet(userStoreKey)) {
        userState = PhoneNumber.State.REGISTERED;
      } else {
        userState = PhoneNumber.State.AFFILIATED;
      }
      return Single.just(userState);
    })
      .compose(singleDelayTransformer());
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
    if (!this.diskStore.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNASSOCIATED_PHONE_NUMBER)
        .build();
      result = Result.create(failureData);
    } else {
      final User user = this.diskStore.get(userStoreKey, User.class);
      final Email userEmail = user.email();
      final Password userPassword = this.diskStore
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
  public Single<SessionData> fetchSessionData() {
    return Single.defer(() -> {
      final SessionData sessionData = SessionData.builder()
        .banks(this.mockData.banks)
        .partners(this.mockData.partners)
        .build();
      return Single.just(sessionData);
    });
  }

  private void updateUserName_(Name name) throws Exception {
    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.diskStore.isSet(userStoreKey)) {
      throw new RuntimeException("Unauthorized");
    } else {
      final User currentUser = this.diskStore.get(userStoreKey, User.class);
      final User newUser = User.builder()
        .phoneNumber(currentUser.phoneNumber())
        .email(currentUser.email())
        .name(name)
        .id(currentUser.id())
        .picture(currentUser.picture())
        .carrier(currentUser.carrier())
        .build();
      this.diskStore.set(userStoreKey, newUser);
    }
  }

  @Override
  public Completable updateUserName(User user, Name name) {
    return Completable.fromAction(() -> this.updateUserName_(name));
  }

  private Uri updateUserPicture_(File picture) throws Exception {
    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.diskStore.isSet(userStoreKey)) {
      throw new RuntimeException("Unauthorized");
    } else {
      final User currentUser = this.diskStore.get(userStoreKey, User.class);
      final Uri currentUserPicture = currentUser.picture();
      if (ObjectHelper.isNotNull(currentUserPicture)) {
        FileHelper.deleteFile(new File(currentUserPicture.getPath()));
      }
      final File newUserPictureFile = FileHelper
        .createIntPicFileCopy(this.context, picture);
      final Uri newUserPictureUri = Uri.fromFile(newUserPictureFile);
      final User newUser = User.builder()
        .phoneNumber(currentUser.phoneNumber())
        .email(currentUser.email())
        .name(currentUser.name())
        .id(currentUser.id())
        .picture(newUserPictureUri)
        .carrier(currentUser.carrier())
        .build();
      this.diskStore.set(userStoreKey, newUser);
      return newUserPictureUri;
    }
  }

  @Override
  public Single<Uri> updateUserPicture(User user, File picture) {
    return Single.defer(() -> Single.just(this.updateUserPicture_(picture)));
  }

  private void updateUserCarrier_(Partner carrier) {
    final String userStoreKey = createUserStoreKey(this.getAccessToken());
    if (!this.diskStore.isSet(userStoreKey)) {
      throw new RuntimeException("Unauthorized");
    } else {
      final User currentUser = this.diskStore.get(userStoreKey, User.class);
      final User newUser = User.builder()
        .phoneNumber(currentUser.phoneNumber())
        .email(currentUser.email())
        .name(currentUser.name())
        .id(currentUser.id())
        .picture(currentUser.picture())
        .carrier(carrier)
        .build();
      this.diskStore.set(userStoreKey, newUser);
    }
  }

  @Override
  public Completable updateUserCarrier(User user, Partner carrier) {
    return Completable.fromAction(() -> this.updateUserCarrier_(carrier));
  }

  private void enableUnlockMethod_(PublicKey publicKey) throws Exception {
    final PhoneNumber userPhoneNumber = this.getAccessToken();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    if (!this.diskStore.isSet(userStoreKey)) {
      throw new IllegalStateException("Unauthorized");
    } else {
      this.diskStore.set(
        createUserPublicKeyStoreKey(userPhoneNumber),
        Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP)
      );
    }
  }

  @Override
  public Completable enableUnlockMethod(PublicKey key) {
    return Completable.fromAction(() -> this.enableUnlockMethod_(key))
      .compose(completableDelayTransformer());
  }

  private Single<Result<User>> openSession_(
    UnlockMethodSignatureData signatureData,
    byte[] signedData
  ) throws Exception {
    final Result<User> result;
    final User user = signatureData.user();
    final PhoneNumber userPhoneNumber = user.phoneNumber();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    final String userPublicKeyStoreKey = createUserPublicKeyStoreKey(userPhoneNumber);
    if (!this.diskStore.isSet(userStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      result = Result.create(failureData);
    } else if (!this.diskStore.isSet(userPublicKeyStoreKey)) {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNEXPECTED)
        .build();
      result = Result.create(failureData);
    } else {
      final String publicKeyBase64 = this.diskStore.get(userPublicKeyStoreKey, String.class);
      final PublicKey publicKey = KeyFactory
        .getInstance(this.configData.keyGenAlgName())
        .generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyBase64, Base64.NO_WRAP)));
      final Signature signature
        = Signature.getInstance(this.configData.signAlgName());
      signature.initVerify(publicKey);
      if (!signatureData.verify(signature, signedData)) {
        final FailureData failureData = FailureData.builder()
          .code(FailureCode.UNEXPECTED)
          .build();
        result = Result.create(failureData);
      } else {
        result = Result.create(user);
      }
    }
    return Single.just(result);
  }

  @Override
  public Single<Result<User>> openSession(
    UnlockMethodSignatureData signatureData,
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

  private void disableUnlockMethod_() throws Exception {
    final PhoneNumber userPhoneNumber = this.getAccessToken();
    final String userStoreKey = createUserStoreKey(userPhoneNumber);
    if (!this.diskStore.isSet(userStoreKey)) {
      throw new IllegalStateException("Unauthorized");
    } else {
      this.diskStore.remove(createUserPublicKeyStoreKey(userPhoneNumber));
    }
  }

  @Override
  public Completable disableUnlockMethod() {
    return Completable.fromAction(this::disableUnlockMethod_)
      .compose(completableDelayTransformer());
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private DiskStore diskStore;
    private Context context;

    private UnlockMethodConfigData configData;

    private MockData mockData;

    private Builder() {
    }

    final Builder accessTokenStore(AccessTokenStore accessTokenStore) {
      this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
      return this;
    }

    final Builder configData(UnlockMethodConfigData configData) {
      this.configData = ObjectHelper.checkNotNull(configData, "configData");
      return this;
    }

    final Builder context(Context context) {
      this.context = ObjectHelper.checkNotNull(context, "context");
      return this;
    }

    final Builder store(DiskStore diskStore) {
      this.diskStore = ObjectHelper.checkNotNull(diskStore, "diskStore");
      return this;
    }

    final Builder mockData(MockData mockData) {
      this.mockData = ObjectHelper.checkNotNull(mockData, "mockData");
      return this;
    }

    final MockApi build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("accessTokenStore", ObjectHelper.isNull(this.accessTokenStore))
        .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
        .addPropertyNameIfMissing("configData", ObjectHelper.isNull(this.configData))
        .addPropertyNameIfMissing("diskStore", ObjectHelper.isNull(this.diskStore))
        .addPropertyNameIfMissing("mockData", ObjectHelper.isNull(this.mockData))
        .checkNoMissingProperties();
      return new MockApi(this);
    }
  }
}
