package com.tpago.movil.data.api;

import android.util.Base64;

import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.domain.KeyValueStoreHelper;
import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
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

  private final KeyValueStore keyValueStore;
  private final AltAuthMethodConfigData altAuthMethodConfigData;

  private AtomicReference<PublicKey> altAuthMethodPublicKeyReference = new AtomicReference<>();

  private MockApi(Builder builder) {
    this.keyValueStore = builder.keyValueStore;
    this.altAuthMethodConfigData = builder.altAuthMethodConfigData;
  }

  private void enableAltAuthMethod_(PublicKey publicKey) throws Exception {
    this.altAuthMethodPublicKeyReference.set(publicKey);
    this.keyValueStore.set(
      KEY_ALT_AUTH_METHOD,
      Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT)
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
        .getInstance(this.altAuthMethodConfigData.signAlgName())
        .generatePublic(
          new X509EncodedKeySpec(
            Base64.decode(
              this.keyValueStore.get(KEY_ALT_AUTH_METHOD),
              Base64.DEFAULT
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
        .code(FailureCode.UNEXPECTED)
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
          .code(FailureCode.UNAUTHORIZED)
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

    private KeyValueStore keyValueStore;
    private AltAuthMethodConfigData altAuthMethodConfigData;

    private Builder() {
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
