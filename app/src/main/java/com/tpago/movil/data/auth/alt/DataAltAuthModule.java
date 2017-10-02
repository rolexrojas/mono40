package com.tpago.movil.data.auth.alt;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.data.api.Api;
import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.domain.auth.alt.AltAuthMethodService;

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
  AltAuthMethodService altAuthService(Api api) {
    return ApiAltAuthMethodService.create(api);
  }

  @Provides
  @Singleton
  AltAuthConfigData altAuthData() {
    final Calendar startCalendar = Calendar.getInstance();

    final Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.YEAR, 50);

    return AltAuthConfigData.builder()
      .providerName("AndroidKeyStore")
      .keyAlias("AltAuth.Key")
      .keyGenAlgName("RSA")
      .keyGenAlgParamSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
      .signAlgName("SHA1withRSA")
      .methodKey("AltAuth.Method")
      .codeMethodKey("AltAuth.Method.Code")
      .codeMethodSubject(new X500Principal(BuildConfig.AUTH_METHOD_CODE_SUBJECT))
      .codeMethodSerialNumber(BigInteger.ONE)
      .codeMethodStartDate(startCalendar.getTime())
      .codeMethodEndDate(endCalendar.getTime())
      .build();
  }

  @Provides
  @Singleton
  KeyStore keyStore(AltAuthConfigData data) {
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
  CodeAuthMethodStore codeAuthMethodStore(KeyValueStore store, AltAuthConfigData configData) {
    return CodeAuthMethodStore.create(store, configData);
  }

  @Provides
  @Singleton
  CodeAltAuthMethodKeyGenerator.Creator codeAuthMethodKeyPairGeneratorCreator(
    Context context,
    CodeAuthMethodStore store,
    AltAuthConfigData configData
  ) {
    return CodeAltAuthMethodKeyGenerator.Creator
      .builder()
      .context(context)
      .store(store)
      .configData(configData)
      .build();
  }

  @Provides
  @Singleton
  CodeAltAuthMethodKeySupplier.Creator codeAuthMethodKeyPairSupplierCreator(
    KeyStore keyStore,
    AltAuthConfigData altAuthConfigData,
    CodeAuthMethodStore codeAuthMethodStore
  ) {
    return CodeAltAuthMethodKeySupplier.Creator
      .builder()
      .keyStore(keyStore)
      .altAuthMethodConfigData(altAuthConfigData)
      .configAuthMethodStore(codeAuthMethodStore)
      .build();
  }

  @Provides
  @Singleton
  AltAuthMethodManager altAuthManager(
    KeyValueStore store,
    AltAuthMethodService service,
    AltAuthConfigData data,
    KeyStore keyStore,
    CodeAuthMethodStore codeAuthMethodStore
  ) {
    return AltAuthMethodManager.builder()
      .store(store)
      .service(service)
      .methodKey(data.methodKey())
      .signAlgName(data.signAlgName())
      .addOnDisabledAction(() -> keyStore.deleteEntry(data.keyAlias()))
      .addOnDisabledAction(codeAuthMethodStore::clear)
      .build();
  }
}
