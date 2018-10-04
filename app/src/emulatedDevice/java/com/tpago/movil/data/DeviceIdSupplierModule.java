package com.tpago.movil.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class DeviceIdSupplierModule {

  @Provides
  @Singleton
  DeviceIdSupplier deviceIdSupplier() {
    return EmulatedDeviceIdSupplier.create();
  }
}
