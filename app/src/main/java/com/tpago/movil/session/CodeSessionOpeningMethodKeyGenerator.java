package com.tpago.movil.session;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import com.tpago.movil.Code;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeSessionOpeningMethodKeyGenerator
  implements SessionOpeningMethodKeyGenerator {

  private final CodeSessionOpeningMethodStore codeStore;
  private final SessionOpeningMethodConfigData configData;
  private final Context context;
  private final KeyStore keyStore;

  private final Code code;

  private CodeSessionOpeningMethodKeyGenerator(Creator creator, Code code) {
    this.codeStore = creator.codeStore;
    this.configData = creator.configData;
    this.context = creator.context;
    this.keyStore = creator.keyStore;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  @Override
  public SessionOpeningMethod method() {
    return SessionOpeningMethod.CODE;
  }

  private KeyPair generateKeyPair() throws Exception {
    final KeyPairGenerator generator = KeyPairGenerator
      .getInstance(this.configData.keyGenAlgName(), this.configData.providerName());
    final AlgorithmParameterSpec algParamSpec = new KeyPairGeneratorSpec.Builder(this.context)
      .setAlias(this.configData.keyAlias())
      .setKeyType(this.configData.keyGenAlgName())
      .setAlgorithmParameterSpec(this.configData.keyGenAlgParamSpec())
      .setSubject(this.configData.codeMethodSubject())
      .setSerialNumber(this.configData.codeMethodSerialNumber())
      .setStartDate(this.configData.codeMethodStartDate())
      .setEndDate(this.configData.codeMethodEndDate())
      .build();
    generator.initialize(algParamSpec);
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
      .map(this::generatePublicKey)
      .doOnSuccess((publicKey) -> this.codeStore.set(publicKey, this.code));
  }

  @Override
  public void rollback() throws Exception {
    this.codeStore.clear();
    if (this.keyStore.containsAlias(this.configData.keyAlias())) {
      this.keyStore.deleteEntry(this.configData.keyAlias());
    }
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final CodeSessionOpeningMethodStore codeStore;
    private final SessionOpeningMethodConfigData configData;
    private final Context context;
    private final KeyStore keyStore;

    private Creator(Builder builder) {
      this.codeStore = builder.codeStore;
      this.configData = builder.configData;
      this.context = builder.context;
      this.keyStore = builder.keyStore;
    }

    public final CodeSessionOpeningMethodKeyGenerator create(Code code) {
      return new CodeSessionOpeningMethodKeyGenerator(this, code);
    }

    static final class Builder {

      private CodeSessionOpeningMethodStore codeStore;
      private SessionOpeningMethodConfigData configData;
      private Context context;
      private KeyStore keyStore;

      private Builder() {
      }

      final Builder codeStore(CodeSessionOpeningMethodStore codeStore) {
        this.codeStore = ObjectHelper.checkNotNull(codeStore, "codeStore");
        return this;
      }

      final Builder configData(SessionOpeningMethodConfigData configData) {
        this.configData = ObjectHelper.checkNotNull(configData, "configData");
        return this;
      }

      final Builder context(Context context) {
        this.context = ObjectHelper.checkNotNull(context, "context");
        return this;
      }

      final Builder keyStore(KeyStore keyStore) {
        this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("codeStore", ObjectHelper.isNull(this.codeStore))
          .addPropertyNameIfMissing("configData", ObjectHelper.isNull(this.configData))
          .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
          .addPropertyNameIfMissing("keyStore", ObjectHelper.isNull(this.keyStore))
          .checkNoMissingProperties();
        return new Creator(this);
      }
    }
  }
}
