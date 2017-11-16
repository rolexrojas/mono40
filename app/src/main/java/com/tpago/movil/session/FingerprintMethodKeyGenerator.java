package com.tpago.movil.session;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.tpago.movil.util.ObjectHelper;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import io.reactivex.Single;

public final class FingerprintMethodKeyGenerator implements UnlockMethodKeyGenerator {

  static FingerprintMethodKeyGenerator create(UnlockMethodConfigData configData) {
    return new FingerprintMethodKeyGenerator(configData);
  }

  private final UnlockMethodConfigData configData;

  private FingerprintMethodKeyGenerator(UnlockMethodConfigData configData) {
    this.configData = ObjectHelper.checkNotNull(configData, "unlockMethodConfigData");
  }

  @Override
  public UnlockMethod method() {
    return UnlockMethod.FINGERPRINT;
  }

  @TargetApi(Build.VERSION_CODES.M)
  private KeyPair generateKeyPair() throws Exception {
    final AlgorithmParameterSpec paramSpec = new KeyGenParameterSpec
      .Builder(this.configData.keyAlias(), KeyProperties.PURPOSE_SIGN)
      .setAlgorithmParameterSpec(this.configData.keyGenAlgParamSpec())
      .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1)
      .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
      .setUserAuthenticationRequired(true)
      .build();
    final KeyPairGenerator generator = KeyPairGenerator
      .getInstance(this.configData.keyGenAlgName(), this.configData.providerName());
    generator.initialize(paramSpec);
    return generator.generateKeyPair();
  }

  /**
   * This conversion is also currently needed on API Level 23 (Android M) due to a platform bug
   * which prevents the use of Android Keystore public keys when their private keys require user
   * authentication. This conversion creates a new public key which is not backed by Android
   * Keystore and thus is not affected by the bug.
   *
   * @see <a href="https://github.com/googlesamples/android-AsymmetricFingerprintDialog/blob/master/Application/src/main/java/com/example/android/asymmetricfingerprintdialog/FingerprintAuthenticationDialogFragment.java#L198">Explanation</a>
   */
  private PublicKey generatePublicKey(PublicKey publicKey) throws Exception {
    return KeyFactory.getInstance(publicKey.getAlgorithm())
      .generatePublic(new X509EncodedKeySpec(publicKey.getEncoded()));
  }

  @Override
  public Single<PublicKey> generate() {
    return Single.defer(() -> Single.just(this.generateKeyPair()))
      .map(KeyPair::getPublic)
      .map(this::generatePublicKey);
  }
}
