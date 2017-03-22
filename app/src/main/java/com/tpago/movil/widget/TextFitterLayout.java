package com.tpago.movil.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.text.BaseTextWatcher;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public class TextFitterLayout extends FrameLayout {
  private static final float SIZE_RATIO = 2F;

  private static void checkChildCount(int childCount) {
    if (childCount == 1) {
      throw new IllegalStateException("childCount == 1");
    }
  }

  private static void checkChildViewType(View child) {
    if (!(child instanceof TextView)) {
      throw new ClassCastException("(child instanceof TextView) == false");
    }
  }

  private static float getTextWidth(CharSequence text, TextPaint paint, float size) {
    final TextPaint copyPaint = new TextPaint(paint);
    copyPaint.setTextSize(size);
    return copyPaint.measureText(text, 0, text.length());
  }

  private TextView childTextView;

  private int minTextSize;
  private int maxTextSize;

  private boolean shouldResize = false;

  public TextFitterLayout(@NonNull Context context) {
    super(context);
    initializeTextFitterLayout(context, null, R.attr.textFitterLayoutStyle);
  }

  public TextFitterLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
    super(context, attributeSet);
    initializeTextFitterLayout(context, attributeSet, R.attr.textFitterLayoutStyle);
  }

  public TextFitterLayout(
    @NonNull Context context,
    @Nullable AttributeSet attributeSet,
    @AttrRes int defaultStyleAttribute) {
    super(context, attributeSet, defaultStyleAttribute);
    initializeTextFitterLayout(context, attributeSet, defaultStyleAttribute);
  }

  private void initializeTextFitterLayout(
    Context context,
    AttributeSet attributeSet,
    int defaultStyleAttribute) {
    final Resources resources = context.getResources();
    final TypedArray styledAttributes = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.TextFitterLayout,
      defaultStyleAttribute,
      R.style.Widget_TextFitterLayout);
    try {
      minTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.TextFitterLayout_minTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_text_fitter_layout_size_min));
      maxTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.TextFitterLayout_maxTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_text_fitter_layout_size_max));
    } finally {
      styledAttributes.recycle();
    }
  }

  private void resizeText() {
    if (!shouldResize) {
      return;
    }
    final int width = getWidth()
      - getPaddingStart()
      - getPaddingEnd()
      - childTextView.getPaddingStart()
      - childTextView.getPaddingEnd();
    if (width <= 0) {
      return;
    }
    CharSequence currentText = childTextView.getText();
    if (Texts.isEmpty(currentText)) {
      return;
    }
    final TransformationMethod transformationMethod = childTextView.getTransformationMethod();
    if (Objects.isNotNull(transformationMethod)) {
      currentText = transformationMethod.getTransformation(currentText, childTextView);
    }
    final TextPaint currentTextPaint = childTextView.getPaint();
    final float currentTextSize = currentTextPaint.getTextSize();
    float textSize = Math.min(currentTextSize, maxTextSize);
    float textWidth = getTextWidth(currentText, currentTextPaint, textSize);
    if (textWidth > width && textSize > minTextSize) {
      while (textWidth > width && textSize > minTextSize) {
        textSize = Math.max(textSize - SIZE_RATIO, minTextSize);
        textWidth = getTextWidth(currentText, currentTextPaint, textSize);
      }
    } else if (textWidth < width && textSize < maxTextSize) {
      while (textWidth < width && textSize < maxTextSize) {
        textSize = Math.min(textSize + SIZE_RATIO, maxTextSize);
        textWidth = getTextWidth(currentText, currentTextPaint, textSize);
      }
    }
    if (textSize != currentTextSize) {
      childTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    shouldResize = false;
  }

  private void initializeChildTextView(View childView) {
    Timber.d("initializeChildTextView(%1$s)", childView);
    childTextView = (TextView) childView;
    childTextView.addTextChangedListener(new BaseTextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        shouldResize = true;
        resizeText();
      }
    });
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (w != oldw || h != oldh) {
      shouldResize = true;
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (changed || shouldResize) {
      resizeText();
    }
    super.onLayout(changed, left, top, right, bottom);
  }

  @Override
  public void addView(View child) {
    checkChildCount(getChildCount());
    checkChildViewType(child);
    super.addView(child);
  }

  @Override
  public void addView(View child, int index) {
    checkChildCount(getChildCount());
    checkChildViewType(child);
    super.addView(child, index);
    initializeChildTextView(child);
  }

  @Override
  public void addView(View child, int width, int height) {
    checkChildCount(getChildCount());
    checkChildViewType(child);
    super.addView(child, width, height);
    initializeChildTextView(child);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    checkChildCount(getChildCount());
    checkChildViewType(child);
    super.addView(child, params);
    initializeChildTextView(child);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    checkChildCount(getChildCount());
    checkChildViewType(child);
    super.addView(child, index, params);
    initializeChildTextView(child);
  }
}
