package com.tpago.movil.dep.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tpago.movil.R;
import com.tpago.movil.dep.text.BaseTextWatcher;

import static com.tpago.movil.dep.text.Texts.checkIfEmpty;
import static com.tpago.movil.dep.Objects.checkIfNotNull;

/**
 * @author hecvasro
 */
@Deprecated
public final class LabelFitterLayout extends FrameLayout {
  private static final float SIZE_RATIO = 2F;

  private static void assertChildCount(int childCount) {
    if (childCount == 1) {
      throw new IllegalStateException("childCount == 1");
    }
  }

  private static void assertChildViewType(View childView) {
    if (!(childView instanceof Label)) {
      throw new ClassCastException("(childView instanceof Label) == false");
    }
  }

  private static float calculateTextWidth(CharSequence text, TextPaint paint, float size) {
    final TextPaint copyPaint = new TextPaint(paint);
    copyPaint.setTextSize(size);
    return copyPaint.measureText(text, 0, text.length());
  }

  private int minTextSize;
  private int maxTextSize;

  private Label childLabel;
  private TextWatcher childLabelTextWatcher;

  private boolean shouldFitText = false;

  public LabelFitterLayout(@NonNull Context context) {
    super(context);
    initializeLabelFitterLayout(context, null, R.attr.labelFitterLayoutStyle);
  }

  public LabelFitterLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
    super(context, attributeSet);
    initializeLabelFitterLayout(context, attributeSet, R.attr.labelFitterLayoutStyle);
  }

  public LabelFitterLayout(
    @NonNull Context context,
    @Nullable AttributeSet attributeSet,
    @AttrRes int defaultStyleAttribute) {
    super(context, attributeSet, defaultStyleAttribute);
    initializeLabelFitterLayout(context, attributeSet, defaultStyleAttribute);
  }

  private void initializeLabelFitterLayout(
    Context context,
    AttributeSet attributeSet,
    int defaultStyleAttribute) {
    // Obtains all attributes from the given attribute set.
    final Resources resources = context.getResources();
    final TypedArray styledAttributes = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.LabelFitterLayout,
      defaultStyleAttribute,
      R.style.Widget_LabelFitterLayout);
    try {
      minTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.LabelFitterLayout_minTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_label_fitter_layout_text_size_min));
      maxTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.LabelFitterLayout_maxTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_label_fitter_layout_text_size_max));
    } finally {
      styledAttributes.recycle();
    }
  }

  private void initializeChildLabel(View childView) {
    if (checkIfNotNull(childLabel) && checkIfNotNull(childLabelTextWatcher)) {
      childLabel.removeTextChangedListener(childLabelTextWatcher);
      childLabelTextWatcher = null;
      childLabel = null;
    }
    childLabel = (Label) childView;
    childLabelTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        shouldFitText = true;
        fitText();
      }
    };
    childLabel.addTextChangedListener(childLabelTextWatcher);
  }

  private void fitText() {
    if (!shouldFitText) {
      return;
    }
    final int width = childLabel.getRight()
      - childLabel.getLeft()
      - childLabel.getCompoundPaddingStart()
      - childLabel.getCompoundPaddingEnd();
    if (width <= 0) {
      return;
    }
    CharSequence currentText = childLabel.getText();
    if (checkIfEmpty(currentText)) {
      return;
    }
    final TransformationMethod transformationMethod = childLabel.getTransformationMethod();
    if (checkIfNotNull(transformationMethod)) {
      currentText = transformationMethod.getTransformation(currentText, this);
    }
    final TextPaint currentTextPaint = childLabel.getPaint();
    final float currentTextSize = currentTextPaint.getTextSize();
    float textSize = maxTextSize;
    float textWidth = calculateTextWidth(currentText, currentTextPaint, textSize);
    if (textWidth > width && textSize > minTextSize) {
      while (textWidth > width && textSize > minTextSize) {
        textSize = Math.max(textSize - SIZE_RATIO, minTextSize);
        textWidth = calculateTextWidth(currentText, currentTextPaint, textSize);
      }
    }
    if (textSize != currentTextSize) {
      childLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    shouldFitText = false;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (w != oldw || h != oldh) {
      shouldFitText = true;
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (changed || shouldFitText) {
      fitText();
    }
  }

  @Override
  public void addView(View childView) {
    assertChildCount(getChildCount());
    assertChildViewType(childView);
    super.addView(childView);
    initializeChildLabel(childView);
  }

  @Override
  public void addView(View childView, int index) {
    assertChildCount(getChildCount());
    assertChildViewType(childView);
    super.addView(childView, index);
    initializeChildLabel(childView);
  }

  @Override
  public void addView(View childView, int width, int height) {
    assertChildCount(getChildCount());
    assertChildViewType(childView);
    super.addView(childView, width, height);
    initializeChildLabel(childView);
  }

  @Override
  public void addView(View childView, ViewGroup.LayoutParams params) {
    assertChildCount(getChildCount());
    assertChildViewType(childView);
    super.addView(childView, params);
    initializeChildLabel(childView);
  }

  @Override
  public void addView(View childView, int index, ViewGroup.LayoutParams params) {
    assertChildCount(getChildCount());
    assertChildViewType(childView);
    super.addView(childView, index, params);
    initializeChildLabel(childView);
  }
}
