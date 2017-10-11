package com.tpago.movil.data.api;

import android.util.Base64;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.KeyValueStoreHelper;
import com.tpago.movil.api.Api;
import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.session.AccessTokenStore;
import com.tpago.movil.user.User;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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

  private static final String KEY_ALT_AUTH_METHOD = KeyValueStoreHelper
    .createKey(MockApi.class, "AltAuthMethod");

  static Builder builder() {
    return new Builder();
  }

  private final AccessTokenStore accessTokenStore;
  private final KeyValueStore keyValueStore;

  private final User user = User.builder()
    .id(1)
    .phoneNumber(PhoneNumber.create("8098829887"))
    .email(Email.create("hecvasro@gmail.com"))
    .firstName("Hector")
    .lastName("Vasquez")
    .build();

  private final AltAuthMethodConfigData altAuthMethodConfigData;
  private AtomicReference<PublicKey> altAuthMethodPublicKeyReference = new AtomicReference<>();

  private MockApi(Builder builder) {
    this.accessTokenStore = builder.accessTokenStore;
    this.keyValueStore = builder.keyValueStore;

    this.altAuthMethodConfigData = builder.altAuthMethodConfigData;
  }

  @Override
  public Single<Result<User>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    return Single.defer(() -> Single.just(Result.create(this.user)))
      .doOnSuccess((result) -> this.accessTokenStore.set(this.user.toString()))
      .compose(singleDelayTransformer());
  }

  @Override
  public Single<Result<User>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    return Single.defer(() -> Single.just(Result.create(this.user)))
      .doOnSuccess((result) -> this.accessTokenStore.set(this.user.toString()))
      .compose(singleDelayTransformer());
  }

  private void enableAltAuthMethod_(PublicKey publicKey) throws Exception {
    this.altAuthMethodPublicKeyReference.set(publicKey);
    this.keyValueStore.set(
      KEY_ALT_AUTH_METHOD,
      Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP)
    );
  }

  @Override
  public Completable enableAltAuthMethod(PublicKey publicKey) {
    return Completable.fromAction(() -> this.enableAltAuthMethod_(publicKey))
      .compose(completableDelayTransformer());
  }

  private void loadAltAuthMethod() throws Exception {
    final boolean isSet = this.keyValueStore.isSet(KEY_ALT_AUTH_METHOD);
    final boolean isNull = ObjectHelper.isNull(this.altAuthMethodPublicKeyReference.get());
    if (isSet && isNull) {
      final PublicKey publicKey = KeyFactory
        .getInstance(this.altAuthMethodConfigData.keyGenAlgName())
        .generatePublic(
          new X509EncodedKeySpec(
            Base64.decode(
              this.keyValueStore.get(KEY_ALT_AUTH_METHOD),
              Base64.NO_WRAP
            )
          )
        );
      this.altAuthMethodPublicKeyReference.set(publicKey);
    }
  }

  private Result<Placeholder> verifyAltAuthMethod_(
    AltAuthMethodVerifyData data,
    byte[] signedData
  ) throws Exception {
    this.loadAltAuthMethod();

    final PublicKey publicKey = this.altAuthMethodPublicKeyReference.get();
    if (ObjectHelper.isNull(publicKey)) {
      final FailureData failureData = FailureData.builder()
        .code(Api.FailureCode.UNAVAILABLE_SERVICE)
        .build();
      return Result.create(failureData);
    } else {
      final Signature signature = Signature.getInstance(this.altAuthMethodConfigData.signAlgName());
      signature.initVerify(publicKey);
      signature.update(data.toByteArray());
      if (signature.verify(signedData)) {
        return Result.create(Placeholder.get());
      } else {
        final FailureData failureData = FailureData.builder()
          .code(Api.FailureCode.UNAVAILABLE_SERVICE)
          .build();
        return Result.create(failureData);
      }
    }
  }

  @Override
  public Single<Result<Placeholder>> verifyAltAuthMethod(
    AltAuthMethodVerifyData data,
    byte[] signedData
  ) {
    return Single.defer(() -> Single.just(this.verifyAltAuthMethod_(data, signedData)))
      .doOnSuccess((result) -> {
        if (result.isSuccessful()) {
          this.accessTokenStore.set(this.user.toString());
        }
      })
      .compose(singleDelayTransformer());
  }

  private void disableAltAuthMethod_() throws Exception {
    this.altAuthMethodPublicKeyReference.set(null);
    this.keyValueStore.remove(KEY_ALT_AUTH_METHOD);
  }

  @Override
  public Completable disableAltAuthMethod() {
    return Completable.fromAction(this::disableAltAuthMethod_)
      .compose(completableDelayTransformer());
  }

  static final class Builder {

    private AccessTokenStore accessTokenStore;
    private KeyValueStore keyValueStore;

    private AltAuthMethodConfigData altAuthMethodConfigData;

    private Builder() {
    }

    final Builder accessTokenStore(AccessTokenStore accessTokenStore) {
      this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
      return this;
    }

    final Builder keyValueStore(KeyValueStore keyValueStore) {
      this.keyValueStore = ObjectHelper.checkNotNull(keyValueStore, "keyValueStore");
      return this;
    }

    final Builder altAuthMethodConfigData(AltAuthMethodConfigData altAuthMethodConfigData) {
      this.altAuthMethodConfigData = ObjectHelper.checkNotNull(
        altAuthMethodConfigData,
        "altAuthMethodConfigData"
      );
      return this;
    }

    final MockApi build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("keyValueStore", ObjectHelper.isNull(this.keyValueStore))
        .addPropertyNameIfMissing(
          "altAuthMethodConfigData",
          ObjectHelper.isNull(this.altAuthMethodConfigData)
        )
        .checkNoMissingProperties();

      return new MockApi(this);
    }
  }
}
