package com.tpago.movil.session;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.tpago.movil.api.Api;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.insurance.micro.MicroInsurancePartnerStore;
import com.tpago.movil.paypal.PayPalAccountStore;
import com.tpago.movil.product.ProductStore;
import com.tpago.movil.product.disbursable.DisbursableProductStore;
import com.tpago.movil.store.DiskStore;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Singleton;
import javax.security.auth.x500.X500Principal;

import dagger.Module;
import dagger.Provides;

@Module
public final class SessionModule {

  @Provides
  @Singleton
  SessionDataLoader sessionDataLoader(
    BankStore bankStore,
    PartnerStore partnerStore,
    ProductStore productStore,
    DisbursableProductStore disbursableProductStore,
    MicroInsurancePartnerStore microInsurancePartnerStore,
    PayPalAccountStore payPalAccountStore,
    Api api
  ) {
    return SessionDataLoader.builder()
      .bankStore(bankStore)
      .partnerStore(partnerStore)
      .productStore(productStore)
      .disbursableProductStore(disbursableProductStore)
      .microInsurancePartnerStore(microInsurancePartnerStore)
      .payPalAccountStore(payPalAccountStore)
      .api(api)
      .build();
  }

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
  UnlockMethodConfigData unlockMethodConfigData() {
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.YEAR, 50);

    return UnlockMethodConfigData.builder()
      .providerName("AndroidKeyStore")
      .keyAlias("UnlockMethodKey")
      .keyGenAlgName("RSA")
      .keyGenAlgParamSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
      .signAlgName("SHA256withRSA")
      .codeMethodSubject(new X500Principal("CN=tPago,OU=IT,O=GCS Systems,C=DO"))
      .codeMethodSerialNumber(BigInteger.ONE)
      .codeMethodStartDate(startCalendar.getTime())
      .codeMethodEndDate(endCalendar.getTime())
      .codeCipherTransformation("RSA/ECB/PKCS1Padding")
      .build();
  }

  @Provides
  @Singleton
  KeyStore keyStore(UnlockMethodConfigData configData) {
    try {
      final KeyStore keyStore = KeyStore.getInstance(configData.providerName());
      keyStore.load(null);
      return keyStore;
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Provides
  @Singleton
  KeyStoreDisableAction keyStoreDisableAction(
    UnlockMethodConfigData configData,
    KeyStore keyStore
  ) {
    return KeyStoreDisableAction.create(configData, keyStore);
  }

  @Provides
  @Singleton
  CodeStore codeStore(UnlockMethodConfigData configData, DiskStore diskStore) {
    return CodeStore.create(configData, diskStore);
  }

  @Provides
  @Singleton
  CodeMethodDisableAction codeMethodDisableAction(CodeStore codeStore) {
    return CodeMethodDisableAction.create(codeStore);
  }

  @Provides
  @Singleton
  CodeMethodKeyGenerator.Creator codeKeyGeneratorCreator(
    CodeStore codeStore,
    UnlockMethodConfigData configData,
    Context context
  ) {
    return CodeMethodKeyGenerator.Creator.builder()
      .codeStore(codeStore)
      .configData(configData)
      .context(context)
      .build();
  }

  @Provides
  @Singleton
  CodeMethodSignatureSupplier.Creator codeSignatureSupplierCreator(
    CodeStore codeStore,
    UnlockMethodConfigData configData,
    KeyStore keyStore
  ) {
    return CodeMethodSignatureSupplier.Creator
      .builder()
      .codeStore(codeStore)
      .configData(configData)
      .keyStore(keyStore)
      .build();
  }

  @Provides
  @Singleton
  FingerprintMethodDisableAction fingerprintMethodDisableAction() {
    return FingerprintMethodDisableAction.create();
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintMethodKeyGenerator fingerprintKeyGenerator(
    FingerprintManagerCompat fingerprintManager,
    UnlockMethodConfigData configData
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintMethodKeyGenerator.create(configData);
    } else {
      return null;
    }
  }

  @Provides
  @Singleton
  @Nullable
  FingerprintMethodSignatureSupplier.Creator fingerprintSignatureSupplierCreator(
    UnlockMethodConfigData configData,
    FingerprintManagerCompat fingerprintManager,
    KeyStore keyStore
  ) {
    if (fingerprintManager.isHardwareDetected()) {
      return FingerprintMethodSignatureSupplier.Creator
        .builder()
        .configData(configData)
        .fingerprintManager(fingerprintManager)
        .keyStore(keyStore)
        .build();
    } else {
      return null;
    }
  }

  @Provides
  @Singleton
  UnlockMethodDisableActionFactory unlockMethodDisableActionFactory(
    KeyStoreDisableAction keyStoreDisableAction,
    CodeMethodDisableAction codeMethodDisableAction,
    FingerprintMethodDisableAction fingerprintMethodDisableAction
  ) {
    final DecoratedMethodDisableAction decoratedCodeMethodDisableAction
      = DecoratedMethodDisableAction.create(keyStoreDisableAction, codeMethodDisableAction);
    final DecoratedMethodDisableAction decoratedFingerprintMethodDisableAction
      = DecoratedMethodDisableAction.create(keyStoreDisableAction, fingerprintMethodDisableAction);
    return UnlockMethodDisableActionFactory.builder()
      .addAction(UnlockMethod.CODE, decoratedCodeMethodDisableAction)
      .addAction(UnlockMethod.FINGERPRINT, decoratedFingerprintMethodDisableAction)
      .build();
  }

  @Provides
  @Singleton
  List<SessionCloseAction> closeActions(BalanceManager balanceManager) {
    final List<SessionCloseAction> actions = new ArrayList<>();
    actions.add(balanceManager::reset);
    return actions;
  }

  @Provides
  @Singleton
  List<SessionDestroyAction> destroyActions(
    ProductManager productManager,
    PosBridge posBridge,
    RecipientManager recipientManager
  ) {
    final List<SessionDestroyAction> actions = new ArrayList<>();
    actions.add((user) -> productManager.clear());
    if (posBridge.isAvailable()) {
      actions.add((user) -> posBridge.unregister(user.phoneNumber()));
    }
    actions.add((user) -> recipientManager.clear());
    return actions;
  }

  @Provides
  @Singleton
  SessionManager sessionManager(
    AccessTokenStore accessTokenStore,
    Api api,
    JobManager jobManager,
    DiskStore diskStore,
    UnlockMethodDisableActionFactory unlockMethodDisableActionFactory,
    List<SessionCloseAction> closeActions,
    List<SessionDestroyAction> destroyActions
  ) {
    return SessionManager.builder()
      .accessTokenStore(accessTokenStore)
      .api(api)
      .jobManager(jobManager)
      .store(diskStore)
      .unlockMethodDisableActionFactory(unlockMethodDisableActionFactory)
      .closeActions(closeActions)
      .destroyActions(destroyActions)
      .build();
  }
}
