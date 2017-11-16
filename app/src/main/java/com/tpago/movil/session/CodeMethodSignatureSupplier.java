package com.tpago.movil.session;

import com.tpago.movil.Code;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeMethodSignatureSupplier
  implements UnlockMethodSignatureSupplier {

  private final CodeStore codeStore;
  private final UnlockMethodConfigData configData;
  private final KeyStore keyStore;

  private final Code code;

  private CodeMethodSignatureSupplier(Creator creator, Code code) {
    this.codeStore = creator.codeStore;
    this.configData = creator.configData;
    this.keyStore = creator.keyStore;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  private Result<PrivateKey> getPrivateKey() throws Exception {
    final PrivateKey privateKey = (PrivateKey) this.keyStore
      .getKey(this.configData.keyAlias(), null);
    final Code code = this.codeStore.get(privateKey);
    if (this.code.equals(code)) {
      return Result.create(privateKey);
    } else {
      final FailureData failureData = FailureData.builder()
        .code(FailureCode.UNAUTHORIZED)
        .build();
      return Result.create(failureData);
    }
  }

  private Result<Signature> getSignature(Result<PrivateKey> result) throws Exception {
    if (result.isSuccessful()) {
      final Signature signature = Signature
        .getInstance(this.configData.signAlgName());
      signature.initSign(result.successData());
      return Result.create(signature);
    } else {
      return Result.create(result.failureData());
    }
  }

  @Override
  public Single<Result<Signature>> get() {
    return Single.defer(() -> Single.just(this.getPrivateKey()))
      .map(this::getSignature);
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final UnlockMethodConfigData configData;
    private final KeyStore keyStore;
    private final CodeStore codeStore;

    private Creator(Builder builder) {
      this.codeStore = builder.codeStore;
      this.configData = builder.configData;
      this.keyStore = builder.keyStore;
    }

    public final CodeMethodSignatureSupplier create(Code code) {
      return new CodeMethodSignatureSupplier(this, code);
    }

    static final class Builder {

      private CodeStore codeStore;
      private UnlockMethodConfigData configData;
      private KeyStore keyStore;

      private Builder() {
      }

      final Builder codeStore(CodeStore codeStore) {
        this.codeStore = ObjectHelper.checkNotNull(codeStore, "codeStore");
        return this;
      }

      final Builder configData(UnlockMethodConfigData configData) {
        this.configData = ObjectHelper.checkNotNull(configData, "unlockMethodConfigData");
        return this;
      }

      final Builder keyStore(KeyStore keyStore) {
        this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("codeStore", ObjectHelper.isNull(this.codeStore))
          .addPropertyNameIfMissing("unlockMethodConfigData", ObjectHelper.isNull(this.configData))
          .addPropertyNameIfMissing("keyStore", ObjectHelper.isNull(this.keyStore))
          .checkNoMissingProperties();
        return new Creator(this);
      }
    }
  }
}
