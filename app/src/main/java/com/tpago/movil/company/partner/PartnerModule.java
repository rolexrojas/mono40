package com.tpago.movil.company.partner;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class PartnerModule {

  @Provides
  @Singleton
  PartnerStore partnerStore() {
    return PartnerStoreMemoized.create();
  }
}
