package com.tpago.movil.d.data;

import android.content.Context;
import androidx.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.tpago.movil.d.domain.DepDeviceManager;
import com.tpago.movil.d.domain.text.TextHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
@Singleton
final class AndroidDeviceManager implements DepDeviceManager {
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
