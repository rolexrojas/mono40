package com.tpago.movil.data;

import android.content.Context;
import android.telephony.TelephonyManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that belong to the data layer of not emulated
 * devices.
 *
 * @author hecvasro
 */
@Module
public final class DataFlavorModule {

  @Provides
  @Singleton
  TelephonyManager telephonyManager(Context context) {
    return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  }

  @Provides
  @Singleton
  DeviceIdSupplier deviceIdSupplier(TelephonyManager telephonyManager) {
    return NotEmulatedDeviceIdSupplier.create(telephonyManager);
  }
}
