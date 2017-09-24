package com.tpago.movil.data;

/**
 * {@link DeviceIdSupplier Supplier} implementation for emulated devices
 * <p>
 * Uses a 15-digit {@link String string} as identifier.
 *
 * @author hecvasro
 */
final class EmulatedDeviceIdSupplier implements DeviceIdSupplier {

  private static final String DEVICE_ID = "012345678901234";

  static EmulatedDeviceIdSupplier create() {
    return new EmulatedDeviceIdSupplier();
  }

  private EmulatedDeviceIdSupplier() {
  }

  @Override
  public String get() {
    return DEVICE_ID;
  }
}
