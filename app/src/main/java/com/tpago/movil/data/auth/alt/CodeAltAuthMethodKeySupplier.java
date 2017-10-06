package com.tpago.movil.data.auth.alt;

import com.tpago.movil.Code;
import com.tpago.movil.domain.auth.alt.AltAuthMethodKeySupplier;
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

  private final AltAuthMethodConfigData altAuthMethodConfigData;
  private final KeyStore keyStore;
  private final CodeAltAuthMethodStore codeAltAuthMethodStore;

  private final Code code;

  private CodeAltAuthMethodKeySupplier(Creator creator, Code code) {
    this.altAuthMethodConfigData = creator.altAuthMethodConfigData;
    this.keyStore = creator.keyStore;
    this.codeAltAuthMethodStore = creator.codeAltAuthMethodStore;

    this.code = ObjectHelper.checkNotNull(code, "code");
  }

  private Result<PrivateKey> getPrivateKey() throws Exception {
    final PrivateKey privateKey = (PrivateKey) this.keyStore
      .getKey(this.altAuthMethodConfigData.keyAlias(), null);
    final Code code = this.codeAltAuthMethodStore.get(privateKey);
    if (this.code.equals(code)) {
      return Result.create(privateKey);
    } else {
      final FailureData failureData = FailureData.builder()
        .code(FailureData.Code.INCORRECT_CODE)
        .build();
      return Result.create(failureData);
    }
  }

  @Override
  public Single<Result<PrivateKey>> get() {
    return Single.defer(() -> Single.just(this.getPrivateKey()));
  }

  public static final class Creator {

    static Builder builder() {
      return new Builder();
    }

    private final AltAuthMethodConfigData altAuthMethodConfigData;
    private final KeyStore keyStore;
    private final CodeAltAuthMethodStore codeAltAuthMethodStore;

    private Creator(Builder builder) {
      this.altAuthMethodConfigData = builder.altAuthMethodConfigData;
      this.keyStore = builder.keyStore;
      this.codeAltAuthMethodStore = builder.codeAltAuthMethodStore;
    }

    public final CodeAltAuthMethodKeySupplier create(Code code) {
      return new CodeAltAuthMethodKeySupplier(this, code);
    }

    static final class Builder {

      private KeyStore keyStore;
      private CodeAltAuthMethodStore codeAltAuthMethodStore;
      private AltAuthMethodConfigData altAuthMethodConfigData;

      private Builder() {
      }

      final Builder altAuthMethodConfigData(AltAuthMethodConfigData altAuthMethodConfigData) {
        this.altAuthMethodConfigData = ObjectHelper.checkNotNull(
          altAuthMethodConfigData,
          "altAuthMethodConfigData"
        );
        return this;
      }

      final Builder keyStore(KeyStore keyStore) {
        this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
        return this;
      }

      final Builder configAuthMethodStore(CodeAltAuthMethodStore codeAltAuthMethodStore) {
        this.codeAltAuthMethodStore = ObjectHelper.checkNotNull(
          codeAltAuthMethodStore,
          "codeAltAuthMethodStore"
        );
        return this;
      }

      final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing(
            "altAuthMethodConfigData",
            ObjectHelper.isNull(this.altAuthMethodConfigData)
          )
          .addPropertyNameIfMissing("keyStore", ObjectHelper.isNull(this.keyStore))
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
