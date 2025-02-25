package com.tpago.movil;

import android.content.Context;

import com.tpago.movil.dep.App;
import com.tpago.movil.util.ObjectHelper;

/**
 * Device display density
 *
 * @author hecvasro
 */
public enum DisplayDensity {
  LDPI(0.75F),
  MDPI(1.00F),
  HDPI(1.50F),
  XHDPI(2.00F),
  XXHDPI(3.00F),
  XXXHDPI(4.00F);

  /**
   * Returns the display density of the device.
   *
   * @param context
   *   {@link App} {@link Context context}.
   *
   * @return Display density of the device.
   */
  public static DisplayDensity get(Context context) {
    ObjectHelper.checkNotNull(context, "context");

    final float value = context
      .getResources()
      .getDisplayMetrics()
      .density;

    if (value <= LDPI.value) {
      return LDPI;
    } else if (value <= MDPI.value) {
      return MDPI;
    } else if (value <= HDPI.value) {
      return HDPI;
    } else if (value <= XHDPI.value) {
      return XHDPI;
    } else if (value <= XXHDPI.value) {
      return XXHDPI;
    } else {
      return XXXHDPI;
    }
  }

  private final float value;

  DisplayDensity(float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.name()
      .toLowerCase();
  }
}
