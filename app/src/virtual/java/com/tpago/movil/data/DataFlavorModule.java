package com.tpago.movil.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that belong to the value layer of emulated
 * devices.
 */
@Module
public final class DataFlavorModule {

  @Provides
  @Singleton
  DeviceIdSupplier deviceIdSupplier() {
    return VirtualDeviceIdSupplier.create();
  }
}
