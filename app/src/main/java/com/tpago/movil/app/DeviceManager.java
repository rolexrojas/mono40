package com.tpago.movil.app;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class DeviceManager {
  private final TelephonyManager telephonyManager;

  public DeviceManager(Context context) {
    telephonyManager = (TelephonyManager) Preconditions.assertNotNull(context, "context == null")
      .getSystemService(Context.TELEPHONY_SERVICE);
  }

  public final String getId() {
//    return telephonyManager.getDeviceId();
    return "352637070606069";
  }
}
