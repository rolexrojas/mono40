package com.tpago.movil.d.ui.misc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.d.ui.Refreshable;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.util.ObjectHelper;

import io.github.inflationx.calligraphy3.TypefaceUtils;


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
   * @param textView
   *   TODO
   * @param styleId
   *   TODO
   */
  public static void setTypeface(@NonNull TextView textView, @StyleRes int styleId) {
    final Context context = textView.getContext();
    final TypedArray array = context
      .obtainStyledAttributes(styleId, new int[]{io.github.inflationx.calligraphy3.R.attr.fontPath});
    try {
      final String fontPath = array.getString(0);
      if (ObjectHelper.isNotNull(fontPath)) {
        textView.setTypeface(TypefaceUtils.load(context.getAssets(), fontPath));
      }
    } finally {
      array.recycle();
    }
  }

  /**
   * TODO
   *
   * @param textView
   *   TODO
   * @param textAppearanceId
   *   TODO
   */
  public static void setTextAppearance(@NonNull TextView textView, @StyleRes int textAppearanceId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      textView.setTextAppearance(textAppearanceId);
    } else {
      textView.setTextAppearance(textView.getContext(), textAppearanceId);
    }
    setTypeface(textView, textAppearanceId);
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
  public static void setColorFilter(
    @NonNull ImageView imageView, @ColorInt int color,
    @NonNull PorterDuff.Mode mode
  ) {
    final Drawable drawable = imageView.getDrawable();
    if (drawable != null) {
      drawable.mutate()
        .setColorFilter(color, mode);
    }
  }

  /**
   * TODO
   *
   * @param refreshable
   *   TODO
   */
  public static void showRefreshIndicator(@NonNull Refreshable refreshable) {
    final LoadIndicator loadIndicator = refreshable.getRefreshIndicator();
    if (ObjectHelper.isNotNull(loadIndicator)) {
      loadIndicator.show();
    }
  }

  /**
   * TODO
   *
   * @param refreshable
   *   TODO
   */
  public static void hideRefreshIndicator(@NonNull Refreshable refreshable) {
    final LoadIndicator loadIndicator = refreshable.getRefreshIndicator();
    if (ObjectHelper.isNotNull(loadIndicator)) {
      loadIndicator.hide();
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
  public static AlertDialog createDialog(
    @NonNull Context context, @NonNull String title,
    @Nullable String message, @Nullable String positiveButtonText,
    @Nullable DialogInterface.OnClickListener positiveButtonOnClickListener,
    @Nullable String negativeButtonText,
    @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener
  ) {
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
    if (ObjectHelper.isNotNull(focusedView)) {
      final InputMethodManager inputMethodManager = (InputMethodManager) activity
        .getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromInputMethod(focusedView.getWindowToken(), 0);
    }
  }
}
