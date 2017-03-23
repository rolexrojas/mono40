package com.tpago.movil.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.tpago.movil.R;
import com.tpago.movil.text.Texts;
import com.tpago.movil.text.Truss;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public final class NotEditableTextInput extends AppCompatTextView implements ErraticView {
  private static final float SIZE_RATIO = 2F;

  private static float getTextWidth(CharSequence text, TextPaint paint, float size) {
    final TextPaint copyPaint = new TextPaint(paint);
    copyPaint.setTextSize(size);
    return copyPaint.measureText(text, 0, text.length());
  }

  private ErraticViewHelper erraticViewHelper;

  private int minTextSize;
  private int maxTextSize;
  private Drawable cursorDrawable;

  private boolean shouldResize = false;

  public NotEditableTextInput(Context context) {
    super(context);
    initializeNotEditableTextInput(context, null, R.attr.notEditableTextInputStyle);
  }

  public NotEditableTextInput(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeNotEditableTextInput(context, attrs, R.attr.notEditableTextInputStyle);
  }

  public NotEditableTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeNotEditableTextInput(context, attrs, defStyleAttr);
  }

  private void initializeNotEditableTextInput(
    Context context,
    AttributeSet attributeSet,
    int defaultStyleAttribute) {
    // Obtains all attributes from the given attribute set.
    final Resources resources = context.getResources();
    final TypedArray styledAttributes = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.NotEditableTextInput,
      defaultStyleAttribute,
      R.style.Widget_NotEditableTextInput_Dark);
    try {
      minTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.NotEditableTextInput_minTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_not_editable_text_input_text_size_min));
      maxTextSize = styledAttributes.getDimensionPixelOffset(
        R.styleable.NotEditableTextInput_maxTextSize,
        resources.getDimensionPixelOffset(R.dimen.widget_not_editable_text_input_text_size_max));
      cursorDrawable = Objects.getDefaultIfNull(
        styledAttributes.getDrawable(R.styleable.NotEditableTextInput_cursorDrawable),
        ContextCompat.getDrawable(context, R.drawable.widget_text_input_dark_cursor));
    } finally {
      styledAttributes.recycle();
    }
    // Initializes the erratic view helper.
    erraticViewHelper = new ErraticViewHelper(this);
  }

  private void resizeText() {
    if (!shouldResize) {
      return;
    }
    final int width = getWidth() - getCompoundPaddingStart() - getCompoundPaddingEnd();
    if (width <= 0) {
      return;
    }
    CharSequence currentText = getText();
    if (Texts.isEmpty(currentText)) {
      return;
    }
    final TransformationMethod transformationMethod = getTransformationMethod();
    if (Objects.isNotNull(transformationMethod)) {
      currentText = transformationMethod.getTransformation(currentText, this);
    }
    final TextPaint currentTextPaint = getPaint();
    final float currentTextSize = currentTextPaint.getTextSize();
    float textSize = maxTextSize;
    float textWidth = getTextWidth(currentText, currentTextPaint, textSize);
    if (textWidth > width && textSize > minTextSize) {
      while (textWidth > width && textSize > minTextSize) {
        textSize = Math.max(textSize - SIZE_RATIO, minTextSize);
        textWidth = getTextWidth(currentText, currentTextPaint, textSize);
      }
    }
    if (textSize != currentTextSize) {
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    shouldResize = false;
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    shouldResize = true;
    resizeText();
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
  protected int[] onCreateDrawableState(int extraSpace) {
    if (Objects.isNull(erraticViewHelper)) {
      return super.onCreateDrawableState(extraSpace);
    } else {
      return erraticViewHelper.onCreateDrawableState(
        super.onCreateDrawableState(erraticViewHelper.getExtraSpace(extraSpace)));
    }
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
    super.setText(
      Truss.create()
        .append(text)
        .pushSpan(new ImageSpan(cursorDrawable))
        .build(),
      type);
  }

  @Override
  public boolean checkIfErraticStateEnabled() {
    return erraticViewHelper.checkIfErraticStateEnabled();
  }

  @Override
  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    erraticViewHelper.setErraticStateEnabled(erraticStateEnabled);
  }
}
