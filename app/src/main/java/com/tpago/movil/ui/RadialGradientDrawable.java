package com.tpago.movil.ui;

import android.graphics.drawable.GradientDrawable;

/**
 * @author hecvasro
 */
public final class RadialGradientDrawable extends GradientDrawable {
  private static final float DEFAULT_CENTER_X = 0.50F;
  private static final float DEFAULT_CENTER_Y = 0.97F;

  public RadialGradientDrawable(int startColor, int endColor, float radius) {
    super(Orientation.TOP_BOTTOM, new int[] { endColor, startColor });
    setGradientType(GradientDrawable.RADIAL_GRADIENT);
    setGradientCenter(DEFAULT_CENTER_X, DEFAULT_CENTER_Y);
    setGradientRadius(radius);
  }
}
