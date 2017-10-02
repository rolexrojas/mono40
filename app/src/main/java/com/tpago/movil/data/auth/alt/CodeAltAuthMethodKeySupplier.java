package com.tpago.movil.data.auth.alt;

import com.tpago.movil.domain.Code;
import com.tpago.movil.domain.auth.alt.AltAuthFailureCode;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.security.KeyStore;
import java.security.PrivateKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeAltAuthMethodKeySupplier implements AltAuthMethodKeySupplier {

  private final AltAuthConfigData altAuthConfigData;
  private final KeyStore keyStore;
  private final CodeAuthMethodStore codeAuthMethodStore;

  private final Code code;

  private CodeAltAuthMethodKeySupplier(Creator creator, Code code) {
    this.altAuthConfigData = creator.altAuthConfigData;
    this.keyStore = creator.keyStore;
    this.codeAuthMethodStore = creator.codeAuthMethodStore;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  private Result<PrivateKey> getResult() throws Exception {
    final PrivateKey privateKey = (PrivateKey) this.keyStore
      .getKey(this.altAuthConfigData.keyAlias(), null);
    final Code code = this.codeAuthMethodStore.get(privateKey);
    if (code.equals(this.code)) {
      return Result.create(privateKey);
    } else {
      final FailureData failureData = FailureData.builder()
        .code(AltAuthFailureCode.UNAUTHORIZED)
        .build();
      return Result.create(failureData);
    }
  }

  @Override
  public Single<Result<PrivateKey>> get() {
    return Single.defer(() -> Single.just(this.getResult()));
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final AltAuthConfigData altAuthConfigData;
    private final KeyStore keyStore;
    private final CodeAuthMethodStore codeAuthMethodStore;

    private Creator(Builder builder) {
      this.altAuthConfigData = builder.altAuthConfigData;
      this.keyStore = builder.keyStore;
      this.codeAuthMethodStore = builder.codeAuthMethodStore;
    }

    public final CodeAltAuthMethodKeySupplier create(Code code) {
      return new CodeAltAuthMethodKeySupplier(this, code);
    }

    static final class Builder {

      private KeyStore keyStore;
      private CodeAuthMethodStore codeAuthMethodStore;
      private AltAuthConfigData altAuthConfigData;

      private Builder() {
      }

      final Builder altAuthMethodConfigData(AltAuthConfigData configData) {
        this.altAuthConfigData = ObjectHelper.checkNotNull(configData, "altAuthConfigData");
        return this;
      }

      final Builder keyStore(KeyStore keyStore) {
        this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
        return this;
      }

      final Builder configAuthMethodStore(CodeAuthMethodStore store) {
        this.codeAuthMethodStore = ObjectHelper.checkNotNull(store, "codeAuthMethodStore");
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing("altAuthConfigData", ObjectHelper.isNull(this.altAuthConfigData))
          .addPropertyNameIfMissing("keyStore", ObjectHelper.isNull(this.keyStore))
          .addPropertyNameIfMissing("codeAuthMethodStore", ObjectHelper.isNull(this.codeAuthMethodStore))
          .checkNoMissingProperties();

        return new Creator(this);
      }
    }
  }
}
