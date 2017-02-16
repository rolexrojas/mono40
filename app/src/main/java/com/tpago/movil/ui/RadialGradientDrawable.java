package com.tpago.movil.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public final class RadialGradientDrawable extends GradientDrawable {
  private static final float DEFAULT_CENTER_X = 0.50F;
  private static final float DEFAULT_CENTER_Y = 0.97F;

  public static void createAndSet(final View view, final int startColor, final int endColor) {
    Objects.checkNotNull(view, "Null view");
    final ViewTreeObserver observer = view.getViewTreeObserver();
    if (observer.isAlive()) {
      observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          final ViewTreeObserver observer = view.getViewTreeObserver();
          if (Objects.isNotNull(observer)) {
            observer.removeOnGlobalLayoutListener(this);
          }
          view.setBackground(new RadialGradientDrawable(startColor, endColor, view.getHeight()));
        }
      });
    }
  }

  private RadialGradientDrawable(int startColor, int endColor, float radius) {
    super(Orientation.TOP_BOTTOM, new int[] { endColor, startColor });
    setGradientType(GradientDrawable.RADIAL_GRADIENT);
    setGradientCenter(DEFAULT_CENTER_X, DEFAULT_CENTER_Y);
    setGradientRadius(radius);
  }
}
