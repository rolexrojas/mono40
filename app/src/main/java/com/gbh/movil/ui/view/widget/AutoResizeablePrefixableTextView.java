package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.gbh.movil.R;
import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 * @see <a href="http://stackoverflow.com/a/5535672">http://stackoverflow.com/a/5535672</a>
 * @see <a href="https://github.com/grantland/android-autofittextview">https://github.com/grantland/android-autofittextview</a>
 */
public class AutoResizeablePrefixableTextView extends PrefixableTextView {
  /**
   * TODO
   */
  private static final int RATIO = 2;

  /**
   * TODO
   */
  private int maxTextSize;
  /**
   * TODO
   */
  private int minTextSize;

  /**
   * TODO
   */
  private boolean mustAdjustText = false;

  /**
   * TODO
   */
  private boolean isAdjusting = false;

  public AutoResizeablePrefixableTextView(Context context) {
    this(context, null);
  }

  public AutoResizeablePrefixableTextView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.autoResizeablePrefixableTextViewStyle);
  }

  public AutoResizeablePrefixableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray array = context.obtainStyledAttributes(attrs,
      R.styleable.AutoResizeablePrefixableTextView, defStyleAttr,
      R.style.App_Widget_AutoResizeablePrefixableTextView);
    try {
      final Resources resources = context.getResources();
      final int defaultMaxTextSize = resources.getDimensionPixelSize(
        R.dimen.app_text_widget_auto_resizeable_prefixable_text_view_content_max);
      final int defaultMinTextSize = resources.getDimensionPixelSize(
        R.dimen.app_text_widget_auto_resizeable_prefixable_text_view_content_min);
      maxTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_maxTextSize, defaultMaxTextSize);
      minTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_minTextSize, defaultMinTextSize);
    } finally {
      array.recycle();
    }
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   * @param paint
   *   TODO
   * @param targetTextSize
   *   TODO
   *
   * @return TODO
   */
  private static float getTextWidth(@NonNull final CharSequence text,
    @NonNull final TextPaint paint, final float targetTextSize) {
    final TextPaint copy = new TextPaint(paint);
    copy.setTextSize(targetTextSize);
    return copy.measureText(text, 0, text.length());
  }

  /**
   * TODO
   */
  private void adjustText() {
    if (mustAdjustText) {
      isAdjusting = true;
      final CharSequence text = getText();
      final int targetWidth = getWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
      if (!TextUtils.isEmpty(text) && targetWidth > 0) {
        final CharSequence targetText;
        final TransformationMethod transformationMethod = getTransformationMethod();
        if (Utils.isNotNull(transformationMethod)) {
          targetText = transformationMethod.getTransformation(text, this);
        } else {
          targetText = text;
        }
        final TextPaint paint = getPaint();
        final float textSize = paint.getTextSize();
        float targetTextSize = Math.min(textSize, maxTextSize);
        float textWidth = getTextWidth(targetText, paint, targetTextSize);
        if (textWidth > targetWidth && targetTextSize > minTextSize) {
          while (textWidth > targetWidth && targetTextSize > minTextSize) {
            targetTextSize = Math.max(targetTextSize - RATIO, minTextSize);
            textWidth = getTextWidth(targetText, paint, targetTextSize);
          }
        } else if (textWidth < targetWidth && targetTextSize < maxTextSize) {
          while (textWidth < targetWidth && targetTextSize < maxTextSize) {
            targetTextSize = Math.min(targetTextSize + RATIO, maxTextSize);
            textWidth = getTextWidth(targetText, paint, targetTextSize);
          }
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
      }
      mustAdjustText = false;
      isAdjusting = false;
    }
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    if (!isAdjusting) {
      mustAdjustText = true;
      adjustText();
    }
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    if (w != oldh || h != oldh) {
      mustAdjustText = true;
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (changed || mustAdjustText) {
      adjustText();
    }
    super.onLayout(changed, left, top, right, bottom);
  }
}
