package com.mono40.movil.company.partner;

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
