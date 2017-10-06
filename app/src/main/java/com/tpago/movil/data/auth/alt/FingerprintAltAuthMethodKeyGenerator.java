package com.tpago.movil.data.auth.alt;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.tpago.movil.domain.auth.alt.AltAuthMethod;
import com.tpago.movil.domain.auth.alt.AltAuthMethodKeyGenerator;
import com.tpago.movil.util.ObjectHelper;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import io.reactivex.Single;

public final class FingerprintAltAuthMethodKeyGenerator implements AltAuthMethodKeyGenerator {

  static FingerprintAltAuthMethodKeyGenerator create(AltAuthMethodConfigData altAuthMethodConfigData) {
    return new FingerprintAltAuthMethodKeyGenerator(altAuthMethodConfigData);
  }

  private final AltAuthMethodConfigData altAuthMethodConfigData;

  private FingerprintAltAuthMethodKeyGenerator(AltAuthMethodConfigData altAuthMethodConfigData) {
    this.altAuthMethodConfigData = ObjectHelper
      .checkNotNull(altAuthMethodConfigData, "altAuhtMethodConfigData");
  }

  @Override
  public AltAuthMethod method() {
    return AltAuthMethod.FINGERPRINT;
  }

  @TargetApi(Build.VERSION_CODES.M)
  private KeyPair generateKeyPair() throws Exception {
    final AlgorithmParameterSpec paramSpec = new KeyGenParameterSpec
      .Builder(this.altAuthMethodConfigData.keyAlias(), KeyProperties.PURPOSE_SIGN)
      .setAlgorithmParameterSpec(this.altAuthMethodConfigData.keyGenAlgParamSpec())
      .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1)
      .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
      .setUserAuthenticationRequired(true)
      .build();

    final KeyPairGenerator generator = KeyPairGenerator
      .getInstance(
        this.altAuthMethodConfigData.keyGenAlgName(),
        this.altAuthMethodConfigData.providerName()
      );
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
