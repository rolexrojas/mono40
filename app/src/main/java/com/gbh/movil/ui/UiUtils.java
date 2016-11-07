package com.gbh.movil.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.gbh.movil.Utils;

/**
 * UI utility methods.
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

  /**
   * TODO
   *
   * @param refreshable
   *   TODO
   */
  public static void showRefreshIndicator(@NonNull Refreshable refreshable) {
    final RefreshIndicator refreshIndicator = refreshable.getRefreshIndicator();
    if (Utils.isNotNull(refreshIndicator)) {
      refreshIndicator.show();
    }
  }

  /**
   * TODO
   *
   * @param refreshable
   *   TODO
   */
  public static void hideRefreshIndicator(@NonNull Refreshable refreshable) {
    final RefreshIndicator refreshIndicator = refreshable.getRefreshIndicator();
    if (Utils.isNotNull(refreshIndicator)) {
      refreshIndicator.hide();
    }
  }
}
