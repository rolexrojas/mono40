package com.tpago.movil.data.auth.alt;

import com.tpago.movil.domain.auth.alt.AltAuthMethodKeySupplier;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.security.KeyStore;
import java.security.PrivateKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class FingerprintAltAuthMethodKeySupplier implements AltAuthMethodKeySupplier {

  static FingerprintAltAuthMethodKeySupplier create(
    AltAuthMethodConfigData altAuthMethodConfigData,
    KeyStore keyStore
  ) {
    return new FingerprintAltAuthMethodKeySupplier(altAuthMethodConfigData, keyStore);
  }

  private final AltAuthMethodConfigData altAuthMethodConfigData;
  private final KeyStore keyStore;

  private FingerprintAltAuthMethodKeySupplier(
    AltAuthMethodConfigData altAuthMethodConfigData,
    KeyStore keyStore
  ) {
    this.altAuthMethodConfigData = ObjectHelper
      .checkNotNull(altAuthMethodConfigData, "altAuhtMethodConfigData");
    this.keyStore = ObjectHelper.checkNotNull(keyStore, "keyStore");
  }

  private Result<PrivateKey> getPrivateKey() throws Exception {
    final PrivateKey privateKey = (PrivateKey) this.keyStore
      .getKey(this.altAuthMethodConfigData.keyAlias(), null);

    return Result.create(privateKey);
  }

  @Override
  public Single<Result<PrivateKey>> get() {
    return Single.defer(() -> Single.just(this.getPrivateKey()));
  }
}
