package com.mono40.movil.data;

import com.mono40.movil.net.NetModule;

import dagger.Module;

/**
 * {@link Module} that contains providers for objects that belong to the value layer.
 *
 * @author hecvasro
 */
@Module(includes = {
  DeviceIdSupplierModule.class,
  NetModule.class,
})
public final class DataModule {
}
