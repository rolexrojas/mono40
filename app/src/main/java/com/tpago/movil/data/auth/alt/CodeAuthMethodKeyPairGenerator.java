package com.tpago.movil.data.auth.alt;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import com.tpago.movil.domain.Code;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.AlgorithmParameterSpec;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeAuthMethodKeyPairGenerator implements AltAuthMethodKeyPairGenerator {

  private final Context context;
  private final CodeAuthMethodStore store;
  private final AltAuthConfigData configData;

  private final Code code;

  private CodeAuthMethodKeyPairGenerator(Creator creator, Code code) {
    this.context = creator.context;
    this.store = creator.store;
    this.configData = creator.configData;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  private KeyPair generate() throws Exception {
    final KeyPairGenerator generator = KeyPairGenerator.getInstance(
      this.configData.keyGenAlgName(),
      this.configData.providerName()
    );

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

  @Override
  public Single<KeyPair> get() {
    return Single.defer(() -> Single.just(this.generate()))
      .doOnSuccess((keyPair) -> this.store.set(keyPair.getPublic(), this.code));
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final Context context;
    private final AltAuthConfigData configData;
    private final CodeAuthMethodStore store;

    private Creator(Builder builder) {
      this.context = builder.context;
      this.configData = builder.configData;
      this.store = builder.store;
    }

    public final CodeAuthMethodKeyPairGenerator create(Code code) {
      return new CodeAuthMethodKeyPairGenerator(this, code);
    }

    static final class Builder {

      private Context context;
      private AltAuthConfigData configData;
      private CodeAuthMethodStore store;

      private Builder() {
      }

      final Builder context(Context context) {
        this.context = ObjectHelper.checkNotNull(context, "context");
        return this;
      }

      final Builder store(CodeAuthMethodStore store) {
        this.store = ObjectHelper.checkNotNull(store, "store");
        return this;
      }

      final Builder configData(AltAuthConfigData configData) {
        this.configData = ObjectHelper.checkNotNull(configData, "configData");
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
          .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
          .addPropertyNameIfMissing("configData", ObjectHelper.isNull(this.configData))
          .checkNoMissingProperties();

        return new Creator(this);
      }
    }
  }
}
