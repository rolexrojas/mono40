package com.tpago.movil.app;

import android.content.Context;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public enum DisplayDensity {
  LDPI(0.75F),
  MDPI(1.00F),
  HDPI(1.50F),
  XHDPI(2.00F),
  XXHDPI(3.00F),
  XXXHDPI(4.00F);

  public static DisplayDensity find(Context context) {
    Preconditions.checkNotNull(context, "context == null");
    final float value = context.getResources().getDisplayMetrics().density;
    if (value <= LDPI.getValue()) {
      return LDPI;
    } else if (value <= MDPI.getValue()) {
      return MDPI;
    } else if (value <= HDPI.getValue()) {
      return HDPI;
    } else if (value <= XHDPI.getValue()) {
      return XHDPI;
    } else if (value <= XXHDPI.getValue()) {
      return XXHDPI;
    } else {
      return XXXHDPI;
    }
  }

  private final float value;

  DisplayDensity(float value) {
    this.value = value;
  }

  public final float getValue() {
    return value;
  }
}
