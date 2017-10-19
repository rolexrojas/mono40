package com.tpago.movil.session;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.api.Api;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.store.Store;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Calendar;

import javax.inject.Singleton;
import javax.security.auth.x500.X500Principal;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class SessionModule {

  @Provides
  @Singleton
  AccessTokenStore accessTokenStore() {
    return AccessTokenStore.create();
  }

  @Provides
  @Singleton
  AccessTokenInterceptor accessTokenInterceptor(AccessTokenStore accessTokenStore) {
    return AccessTokenInterceptor.create(accessTokenStore);
  }

  @Provides
  @Singleton
  SessionManager sessionManager(
    AccessTokenStore accessTokenStore,
    Api api,
    JobManager jobManager,
    Store store,
    RecipientManager recipientManager,
    ProductManager productManager,
    PosBridge posBridge
  ) {
    return SessionManager.builder()
      .accessTokenStore(accessTokenStore)
      .api(api)
      .jobManager(jobManager)
      .store(store)
      .addDestroyAction((user) -> productManager.clear())
      .addDestroyAction((user) -> recipientManager.clear())
      .addDestroyAction(
        (user) -> posBridge.unregister(
          user.phoneNumber()
            .value()
        )
      )
      .build();
  }

  @Provides
  @Singleton
  SessionOpeningMethodConfigData configData() {
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.YEAR, 50);
    return SessionOpeningMethodConfigData.builder()
      .providerName("AndroidKeyStore")
      .keyAlias("AltAuth.Key")
      .keyGenAlgName("RSA")
      .keyGenAlgParamSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
      .signAlgName("SHA256withRSA")
      .codeMethodSubject(new X500Principal(BuildConfig.AUTH_METHOD_CODE_SUBJECT))
      .codeMethodSerialNumber(BigInteger.ONE)
      .codeMethodStartDate(startCalendar.getTime())
      .codeMethodEndDate(endCalendar.getTime())
      .codeCipherTransformation("RSA/ECB/PKCS1Padding")
      .build();
  }

  @Provides
  @Singleton
  KeyStore keyStore(SessionOpeningMethodConfigData data) {
    try {
      final KeyStore keyStore = KeyStore.getInstance(data.providerName());
      keyStore.load(null);
      return keyStore;
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Provides
  @Singleton
  CodeSessionOpeningMethodStore codeStore(Store store, SessionOpeningMethodConfigData configData) {
    return CodeSessionOpeningMethodStore.create(store, configData);
  }

  @Provides
  @Singleton
  CodeSessionOpeningMethodKeyGenerator.Creator codeKeyGeneratorCreator(
    CodeSessionOpeningMethodStore codeStore,
    SessionOpeningMethodConfigData configData,
    Context context,
    KeyStore keyStore
  ) {
    return CodeSessionOpeningMethodKeyGenerator.Creator.builder()
      .codeStore(codeStore)
      .configData(configData)
      .context(context)
      .keyStore(keyStore)
      .build();
  }

  @Provides
  @Singleton
  CodeSessionOpeningMethodSignatureSupplier.Creator codeSignatureSupplierCreator(
    CodeSessionOpeningMethodStore codeStore,
    SessionOpeningMethodConfigData configData,
    KeyStore keyStore
  ) {
    return CodeSessionOpeningMethodSignatureSupplier.Creator.builder()
      .codeStore(codeStore)
      .configData(configData)
      .keyStore(keyStore)
      .build();
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintSessionOpeningMethodKeyGenerator fingerprintKeyGenerator(
    FingerprintManagerCompat fingerprintManager,
    SessionOpeningMethodConfigData configData,
    KeyStore keyStore
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintSessionOpeningMethodKeyGenerator.create(configData, keyStore);
    } else {
      return null;
    }
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintSessionOpeningMethodSignatureSupplier.Creator fingerprintSignatureSupplierCreator(
    SessionOpeningMethodConfigData configData,
    FingerprintManagerCompat fingerprintManager,
    KeyStore keyStore
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintSessionOpeningMethodSignatureSupplier.Creator.builder()
        .configData(configData)
        .fingerprintManager(fingerprintManager)
        .keyStore(keyStore)
        .build();
    } else {
      return null;
    }
  }
}
