package com.tpago.movil.data;

/**
 * {@link DeviceIdSupplier Supplier} implementation for virtual devices
 * <p>
 * Uses a 15-digit {@link String string} as identifier.
 *
 * @author hecvasro
 */
final class VirtualDeviceIdSupplier implements DeviceIdSupplier {

  private static final String DEVICE_ID = "012345678901234";

  static VirtualDeviceIdSupplier create() {
    return new VirtualDeviceIdSupplier();
  }

  private VirtualDeviceIdSupplier() {
  }

  @Override
  public String get() {
    return DEVICE_ID;
  }
}
