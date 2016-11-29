package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gbh.movil.R;

import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AutoResizeablePrefixableTextView extends PrefixableTextView
  implements View.OnLayoutChangeListener {
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
  private int prefixMaxTextSize;
  /**
   * TODO
   */
  private int prefixMinTextSize;

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
      final int defaultPrefixMaxTextSize = resources.getDimensionPixelSize(
        R.dimen.app_text_widget_auto_resizeable_prefixable_text_view_prefix_max);
      final int defaultPrefixMinTextSize = resources.getDimensionPixelSize(
        R.dimen.app_text_widget_auto_resizeable_prefixable_text_view_prefix_min);
      maxTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_maxTextSize, defaultMaxTextSize);
      minTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_minTextSize, defaultMinTextSize);
      prefixMaxTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_prefixMaxTextSize, defaultPrefixMaxTextSize);
      prefixMinTextSize = array.getDimensionPixelSize(
        R.styleable.AutoResizeablePrefixableTextView_prefixMinTextSize, defaultPrefixMinTextSize);
    } finally {
      array.recycle();
    }
  }

  /**
   * TODO
   */
  private void resizeText() {
    final int width = getWidth() - getPaddingLeft() - getPaddingRight();
    if (width > 0) {
      Timber.d("width = %1$d", width);
      final Paint paint = getPaint();
      final CharSequence text = getText();
      final float textWidth = paint.measureText(text, 0, text.length());
      Timber.d("textWidth = %1$f", textWidth);
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    addOnLayoutChangeListener(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeOnLayoutChangeListener(this);
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    resizeText();
  }

  @Override
  public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft,
    int oldTop, int oldRight, int oldBottom) {
    resizeText();
  }
}
