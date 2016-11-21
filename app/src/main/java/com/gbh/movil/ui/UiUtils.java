package com.gbh.movil.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.gbh.movil.Utils;
import com.gbh.movil.ui.view.widget.RefreshIndicator;

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

  /**
   * TODO
   *
   * @param context
   *   TODO
   * @param title
   *   TODO
   * @param message
   *   TODO
   * @param positiveButtonText
   *   TODO
   * @param positiveButtonOnClickListener
   *   TODO
   * @param negativeButtonText
   *   TODO
   * @param negativeButtonOnClickListener
   *   TODO
   *
   * @return TODO
   */
  public static AlertDialog createDialog(@NonNull Context context, @NonNull String title,
    @Nullable String message, @NonNull String positiveButtonText,
    @Nullable DialogInterface.OnClickListener positiveButtonOnClickListener,
    @Nullable String negativeButtonText,
    @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener) {
    return new AlertDialog.Builder(context)
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton(positiveButtonText, positiveButtonOnClickListener)
      .setNegativeButton(negativeButtonText, negativeButtonOnClickListener)
      .create();
  }

  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  public static void closeKeyboard(@NonNull Activity activity) {
    final View focusedView = activity.getCurrentFocus();
    if (Utils.isNotNull(focusedView)) {
      final InputMethodManager inputMethodManager = (InputMethodManager) activity
        .getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromInputMethod(focusedView.getWindowToken(), 0);
    }
  }
}
