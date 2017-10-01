package com.tpago.movil.data.auth.alt;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.data.api.Api;
import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.domain.auth.alt.AltAuthManager;
import com.tpago.movil.domain.auth.alt.AltAuthService;

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
  AltAuthService altAuthService(Api api) {
    return ApiAltAuthService.create(api);
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
      return KeyStore.getInstance(data.providerName());
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
  CodeAuthMethodKeyPairGenerator.Creator codeAuthMethodKeyPairGenerator(
    Context context,
    CodeAuthMethodStore store,
    AltAuthConfigData configData
  ) {
    return CodeAuthMethodKeyPairGenerator.Creator
      .builder()
      .context(context)
      .store(store)
      .configData(configData)
      .build();
  }

  @Provides
  @Singleton
  AltAuthManager altAuthManager(
    KeyValueStore store,
    AltAuthService service,
    AltAuthConfigData data,
    KeyStore keyStore,
    CodeAuthMethodStore codeAuthMethodStore
  ) {
    return AltAuthManager.builder()
      .store(store)
      .service(service)
      .methodKey(data.methodKey())
      .signAlgName(data.signAlgName())
      .addOnDisabledAction(() -> keyStore.deleteEntry(data.keyAlias()))
      .addOnDisabledAction(codeAuthMethodStore::clear)
      .build();
  }
}
