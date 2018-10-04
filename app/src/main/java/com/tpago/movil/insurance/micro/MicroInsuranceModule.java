package com.tpago.movil.insurance.micro;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class MicroInsuranceModule {

  @Provides
  @Singleton
  MicroInsurancePartnerStore microInsurancePartnerStore() {
    return MicroInsurancePartnerStoreMemoized.create();
  }
}
