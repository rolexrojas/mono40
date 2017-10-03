package com.tpago.movil.data.auth.alt;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import com.tpago.movil.domain.Code;
import com.tpago.movil.domain.auth.alt.AltAuthMethod;
import com.tpago.movil.domain.auth.alt.AltAuthMethodKeyGenerator;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeAltAuthMethodKeyGenerator implements AltAuthMethodKeyGenerator {

  private final Context context;
  private final AltAuthMethodConfigData altAuthMethodConfigData;
  private final CodeAltAuthMethodStore codeAltAuthMethodStore;

  private final Code code;

  private CodeAltAuthMethodKeyGenerator(Creator creator, Code code) {
    this.context = creator.context;
    this.altAuthMethodConfigData = creator.altAuthMethodConfigData;
    this.codeAltAuthMethodStore = creator.codeAltAuthMethodStore;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  @Override
  public AltAuthMethod method() {
    return AltAuthMethod.CODE;
  }

  private KeyPair generateKeyPair() throws Exception {
    final KeyPairGenerator generator = KeyPairGenerator.getInstance(
      this.altAuthMethodConfigData.keyGenAlgName(),
      this.altAuthMethodConfigData.providerName()
    );

    final AlgorithmParameterSpec algParamSpec = new KeyPairGeneratorSpec.Builder(this.context)
      .setAlias(this.altAuthMethodConfigData.keyAlias())
      .setKeyType(this.altAuthMethodConfigData.keyGenAlgName())
      .setAlgorithmParameterSpec(this.altAuthMethodConfigData.keyGenAlgParamSpec())
      .setSubject(this.altAuthMethodConfigData.codeMethodSubject())
      .setSerialNumber(this.altAuthMethodConfigData.codeMethodSerialNumber())
      .setStartDate(this.altAuthMethodConfigData.codeMethodStartDate())
      .setEndDate(this.altAuthMethodConfigData.codeMethodEndDate())
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
      .doOnSuccess((publicKey) -> this.codeAltAuthMethodStore.set(publicKey, this.code));
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final Context context;
    private final AltAuthMethodConfigData altAuthMethodConfigData;
    private final CodeAltAuthMethodStore codeAltAuthMethodStore;

    private Creator(Builder builder) {
      this.context = builder.context;
      this.altAuthMethodConfigData = builder.altAuthMethodConfigData;
      this.codeAltAuthMethodStore = builder.codeAltAuthMethodStore;
    }

    public final CodeAltAuthMethodKeyGenerator create(Code code) {
      return new CodeAltAuthMethodKeyGenerator(this, code);
    }

    static final class Builder {

      private Context context;
      private AltAuthMethodConfigData altAuthMethodConfigData;
      private CodeAltAuthMethodStore codeAltAuthMethodStore;

      private Builder() {
      }

      final Builder context(Context context) {
        this.context = ObjectHelper.checkNotNull(context, "context");
        return this;
      }

      final Builder altAuthMethodConfigData(AltAuthMethodConfigData altAuthMethodConfigData) {
        this.altAuthMethodConfigData = ObjectHelper.checkNotNull(
          altAuthMethodConfigData,
          "altAuthMethodConfigData"
        );
        return this;
      }

      final Builder codeAltAuthMethodStore(CodeAltAuthMethodStore codeAltAuthMethodStore) {
        this.codeAltAuthMethodStore = ObjectHelper.checkNotNull(
          codeAltAuthMethodStore,
          "codeAltAuthMethodStore"
        );
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
          .addPropertyNameIfMissing(
            "altAuthMethodConfigData",
            ObjectHelper.isNull(this.altAuthMethodConfigData)
          )
          .addPropertyNameIfMissing(
            "codeAltAuthMethodStore",
            ObjectHelper.isNull(this.codeAltAuthMethodStore)
          )
          .checkNoMissingProperties();

        return new Creator(this);
      }
    }
  }
}
