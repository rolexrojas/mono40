package com.tpago.movil.dep;

import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class DeviceManager {

  public static DeviceManager create(DeviceIdSupplier deviceIdSupplier) {
    return new DeviceManager(deviceIdSupplier);
  }

  private final DeviceIdSupplier deviceIdSupplier;

  private DeviceManager(DeviceIdSupplier deviceIdSupplier) {
    this.deviceIdSupplier = ObjectHelper.checkNotNull(deviceIdSupplier, "deviceIdSupplier");
  }

  public final String getId() {
    return this.deviceIdSupplier.get();
  }
}
