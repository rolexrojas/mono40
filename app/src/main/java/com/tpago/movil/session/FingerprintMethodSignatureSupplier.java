package com.tpago.movil.session;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class FingerprintMethodSignatureSupplier implements UnlockMethodSignatureSupplier {

  private final UnlockMethodConfigData configData;
  private final KeyStore keyStore;
  private final FingerprintManagerCompat fingerprintManager;
  private final CancellationSignal cancellationSignal;

  private FingerprintMethodSignatureSupplier(Creator creator, CancellationSignal cancellationSignal) {
    this.configData = creator.configData;
    this.keyStore = creator.keyStore;
    this.fingerprintManager = creator.fingerprintManager;

    this.cancellationSignal = ObjectHelper.checkNotNull(cancellationSignal, "cancellationSignal");
  }

  private Single<Result<PrivateKey>> getPrivateKey() throws Exception {
    final PrivateKey privateKey = (PrivateKey) this.keyStore
      .getKey(this.configData.keyAlias(), null);
    return Single.just(Result.create(privateKey));
  }

  private Result<Signature> createSignature(Result<PrivateKey> result) throws Exception {
    if (result.isSuccessful()) {
      final Signature signature = Signature.getInstance(this.configData.signAlgName());
      signature.initSign(result.successData());
      return Result.create(signature);
    } else {
      return Result.create(result.failureData());
    }
  }

  private Single<Result<Signature>> authenticateSignature(Result<Signature> result)
    throws Exception {
    if (result.isSuccessful()) {
      final FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat
        .CryptoObject(result.successData());
      return Single.fromPublisher(
        SignaturePublisher.create(this.fingerprintManager, cryptoObject, this.cancellationSignal)
      );
    } else {
      return Single.just(Result.create(result.failureData()));
    }
  }

  @Override
  public Single<Result<Signature>> get() {
    return Single.defer(this::getPrivateKey)
      .map(this::createSignature)
      .flatMap(this::authenticateSignature);
  }

  private static final class SignaturePublisher implements Publisher<Result<Signature>> {

    private static SignaturePublisher create(
      FingerprintManagerCompat fingerprintManager,
      FingerprintManagerCompat.CryptoObject cryptoObject,
      CancellationSignal cancellationSignal
    ) {
      return new SignaturePublisher(
        fingerprintManager,
        cryptoObject,
        cancellationSignal
      );
    }

    private final FingerprintManagerCompat fingerprintManager;
    private final FingerprintManagerCompat.CryptoObject cryptoObject;
    private final CancellationSignal cancellationSignal;

    private SignaturePublisher(
      FingerprintManagerCompat fingerprintManager,
      FingerprintManagerCompat.CryptoObject cryptoObject,
      CancellationSignal cancellationSignal
    ) {
      this.fingerprintManager = fingerprintManager;
      this.cryptoObject = cryptoObject;
      this.cancellationSignal = cancellationSignal;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void subscribe(final Subscriber<? super Result<Signature>> subscriber) {
      this.fingerprintManager
        .authenticate(
          this.cryptoObject,
          0,
          this.cancellationSignal,
          new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
              final FailureData failureData = FailureData.builder()
                .code(FailureCode.UNEXPECTED)
                .build();
              subscriber.onNext(Result.<Signature>create(failureData));
              subscriber.onComplete();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
              final FailureData failureData = FailureData.builder()
                .description("No se pudo leer la huella digital introducida o la misma no está registrada en el dispositivo.")
                .code(FailureCode.UNEXPECTED)
                .build();
              subscriber.onNext(Result.<Signature>create(failureData));
              subscriber.onComplete();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
              final Signature signature = result.getCryptoObject()
                .getSignature();
              subscriber.onNext(Result.create(signature));
              subscriber.onComplete();
            }

            @Override
            public void onAuthenticationFailed() {
              final FailureData failureData = FailureData.builder()
                .code(FailureCode.UNAUTHORIZED)
                .build();
              subscriber.onNext(Result.<Signature>create(failureData));
              subscriber.onComplete();
            }
          },
          null
        );
    }
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final UnlockMethodConfigData configData;
    private final KeyStore keyStore;
    private final FingerprintManagerCompat fingerprintManager;

    private Creator(Builder builder) {
      this.configData = builder.configData;
      this.keyStore = builder.keyStore;
      this.fingerprintManager = builder.fingerprintManager;
    }

    public final FingerprintMethodSignatureSupplier create(CancellationSignal cancellationSignal) {
      return new FingerprintMethodSignatureSupplier(this, cancellationSignal);
    }

    static final class Builder {

      private UnlockMethodConfigData configData;
      private KeyStore keyStore;
      private FingerprintManagerCompat fingerprintManager;

      private Builder() {
      }

      final Builder configData(UnlockMethodConfigData configData) {
        this.configData = ObjectHelper.checkNotNull(configData, "unlockMethodConfigData");
        return this;
      }

      final Builder keyStore(KeyStore keyStore) {
        this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
        return this;
      }

      final Builder fingerprintManager(FingerprintManagerCompat fingerprintManager) {
        this.fingerprintManager = ObjectHelper
          .checkNotNull(fingerprintManager, "fingerprintManager");
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("unlockMethodConfigData", ObjectHelper.isNull(this.configData))
          .addPropertyNameIfMissing("keyStore", ObjectHelper.isNull(this.keyStore))
          .addPropertyNameIfMissing("fingerprintManager", ObjectHelper.isNull(this.fingerprintManager))
          .checkNoMissingProperties();
        return new Creator(this);
      }
    }
  }
}
