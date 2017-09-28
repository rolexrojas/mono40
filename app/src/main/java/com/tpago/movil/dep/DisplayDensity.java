package com.tpago.movil.dep;

import android.content.Context;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Device display density
 *
 * @author hecvasro
 */
@Deprecated
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
    checkNotNull(context, "context == null");

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
