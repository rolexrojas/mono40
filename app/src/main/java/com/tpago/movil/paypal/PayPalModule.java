package com.tpago.movil.paypal;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class PayPalModule {

  @Provides
  @Singleton
  PayPalAccountStore payPalAccountStore() {
    return PayPalAccountStoreMemoized.create();
  }
}
