package com.tpago.movil.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public final class Backgrounds {
  private static final float DRAWABLE_VIEW_HEIGHT_PERCENTAGE = 0.97F;

  public static Drawable darkColoredRadialGradient(Context context, int viewHeight) {
    if (Objects.isNull(context)) {
      throw new NullPointerException("Null context");
    }
    return new RadialGradientDrawable(
      ContextCompat.getColor(context, R.color.background_dark_colored_start),
      ContextCompat.getColor(context, R.color.background_dark_colored_end),
      viewHeight * DRAWABLE_VIEW_HEIGHT_PERCENTAGE);
  }
}
