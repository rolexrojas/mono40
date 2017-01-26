package com.tpago.movil.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.tpago.movil.domain.DeviceManager;
import com.tpago.movil.domain.text.TextHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @author hecvasro
 */
@Singleton
final class AndroidDeviceManager implements DeviceManager {
  private final TelephonyManager telephonyManager;

  /**
   * TODO
   */
  private String id;

  /**
   * TODO
   *
   * @param context
   *   TODO
   */
  @Inject
  AndroidDeviceManager(@NonNull Context context) {
    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  }

  @NonNull
  @Override
  public String getId() {
    if (TextHelper.isEmpty(id)) {
      id = telephonyManager.getDeviceId();
    }
    return id;
  }
}
