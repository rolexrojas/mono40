package com.tpago.movil.data;

import android.telephony.TelephonyManager;

import com.tpago.movil.util.ObjectHelper;

/**
 * {@link DeviceIdSupplier Supplier} implementation for not emulated devices
 * <p>
 * Uses the {@link TelephonyManager#getDeviceId() IMEI} of the device as identifier.
 */
final class PhysicalDeviceIdSupplier implements DeviceIdSupplier {

  static PhysicalDeviceIdSupplier create(TelephonyManager telephonyManager) {
    return new PhysicalDeviceIdSupplier(telephonyManager);
  }

  private final TelephonyManager telephonyManager;

  private PhysicalDeviceIdSupplier(TelephonyManager telephonyManager) {
    this.telephonyManager = ObjectHelper.checkNotNull(telephonyManager, "telephonyManager");
  }

  @Override
  public String get() {
    return this.telephonyManager.getDeviceId();
  }
}
