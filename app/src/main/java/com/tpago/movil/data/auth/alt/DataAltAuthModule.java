package com.tpago.movil.data.auth.alt;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.api.Api;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;

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
public final class DataAltAuthModule {

  @Provides
  @Singleton
  AltAuthMethodConfigData altAuthMethodConfigData() {
    final Calendar startCalendar = Calendar.getInstance();

    final Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.YEAR, 50);

    return AltAuthMethodConfigData.builder()
      .providerName("AndroidKeyStore")
      .keyAlias("AltAuth.Key")
      .keyGenAlgName("RSA")
      .keyGenAlgParamSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
      .signAlgName("SHA1withRSA")
      .codeMethodSubject(new X500Principal(BuildConfig.AUTH_METHOD_CODE_SUBJECT))
      .codeMethodSerialNumber(BigInteger.ONE)
      .codeMethodStartDate(startCalendar.getTime())
      .codeMethodEndDate(endCalendar.getTime())
      .codeCipherTransformation("RSA/ECB/PKCS1Padding")
      .build();
  }

  @Provides
  @Singleton
  KeyStore keyStore(AltAuthMethodConfigData data) {
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
  CodeAltAuthMethodStore codeAltAuthMethodStore(
    KeyValueStore keyValueStore,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    return CodeAltAuthMethodStore.create(keyValueStore, altAuthMethodConfigData);
  }

  @Provides
  @Singleton
  CodeAltAuthMethodKeyGenerator.Creator codeAuthMethodKeyPairGeneratorCreator(
    Context context,
    AltAuthMethodConfigData configData,
    CodeAltAuthMethodStore store
  ) {
    return CodeAltAuthMethodKeyGenerator.Creator.builder()
      .context(context)
      .altAuthMethodConfigData(configData)
      .codeAltAuthMethodStore(store)
      .build();
  }

  @Provides
  @Singleton
  CodeAltAuthMethodKeySupplier.Creator codeAuthMethodKeyPairSupplierCreator(
    KeyStore keyStore,
    AltAuthMethodConfigData altAuthMethodConfigData,
    CodeAltAuthMethodStore codeAltAuthMethodStore
  ) {
    return CodeAltAuthMethodKeySupplier.Creator.builder()
      .keyStore(keyStore)
      .altAuthMethodConfigData(altAuthMethodConfigData)
      .configAuthMethodStore(codeAltAuthMethodStore)
      .build();
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintAltAuthMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator(
    FingerprintManagerCompat fingerprintManager,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintAltAuthMethodKeyGenerator.create(altAuthMethodConfigData);
    } else {
      return null;
    }
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintAltAuthMethodKeySupplier fingerprintAltAuthMethodKeySupplier(
    FingerprintManagerCompat fingerprintManager,
    AltAuthMethodConfigData altAuthMethodConfigData,
    KeyStore keyStore
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintAltAuthMethodKeySupplier.create(altAuthMethodConfigData, keyStore);
    } else {
      return null;
    }
  }

  @Provides
  @Singleton
  AltAuthMethodManager altAuthManager(
    KeyValueStore store,
    Api api,
    AltAuthMethodConfigData data,
    KeyStore keyStore,
    CodeAltAuthMethodStore codeAltAuthMethodStore
  ) {
    return AltAuthMethodManager.builder()
      .keyValueStore(store)
      .api(api)
      .signAlgName(data.signAlgName())
      .addRollbackAction(() -> keyStore.deleteEntry(data.keyAlias()))
      .addRollbackAction(codeAltAuthMethodStore::clear)
      .build();
  }
}
