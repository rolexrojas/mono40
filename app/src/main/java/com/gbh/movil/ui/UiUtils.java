package com.gbh.movil.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class UiUtils {
  private UiUtils() {
  }

  /**
   * TODO
   *
   * @param context
   *   TODO
   * @param colorId
   *   TODO
   *
   * @return TODO
   */
  @ColorInt
  public static int getColor(@NonNull Context context, @ColorRes int colorId) {
    return ContextCompat.getColor(context, colorId);
  }

  /**
   * TODO
   *
   * @param imageView
   *   TODO
   * @param color
   *   TODO
   */
  public static void setColorFilter(@NonNull ImageView imageView, @ColorInt int color,
    @NonNull PorterDuff.Mode mode) {
    final Drawable drawable = imageView.getDrawable();
    if (drawable != null) {
      drawable.mutate().setColorFilter(color, mode);
    }
  }
}
