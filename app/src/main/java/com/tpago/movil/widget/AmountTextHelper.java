package com.tpago.movil.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class AmountTextHelper implements AmountText {
  private final Context context;

  private String currencyContent;
  private String currencyFontPath;
  private int currencyTextColor;
  private int currencyTextSize;
  private String valueContent;
  private String valueFontPath;
  private int valueTextColor;
  private int valueTextSize;

  private TextView currencyTextView;
  private TextView valueTextView;

  public AmountTextHelper(Context context, AttributeSet attributeSet, int defaultStyleAttribute) {
    this.context = Preconditions.checkNotNull(context, "context == null");
    final Resources resources = this.context.getResources();
    final TypedArray a = this.context.obtainStyledAttributes(
        attributeSet,
        R.styleable.AmountText,
        defaultStyleAttribute,
        R.style.D_App_Widget_AmountTextView);
    try {
      currencyContent = a.getString(R.styleable.AmountText_currencyContent);
      currencyFontPath = Objects.getDefaultIfNull(
        a.getString(R.styleable.AmountText_currencyFontPath),
        this.context.getString(R.string.amount_text_currency_font));
      currencyTextColor = a.getColor(
        R.styleable.AmountText_currencyTextColor,
        ContextCompat.getColor(this.context, R.color.widget_amount_text_currency));
      currencyTextSize = a.getDimensionPixelOffset(
        R.styleable.AmountText_currencyTextSize,
        resources.getDimensionPixelOffset(R.dimen.amount_text_currency));
      valueContent = a.getString(R.styleable.AmountText_valueContent);
      valueFontPath = Objects.getDefaultIfNull(
        a.getString(R.styleable.AmountText_valueFontPath),
        this.context.getString(R.string.amount_text_value_font));
      valueTextColor = a.getColor(
        R.styleable.AmountText_valueTextColor,
        ContextCompat.getColor(this.context, R.color.widget_amount_text_value));
      valueTextSize = a.getDimensionPixelOffset(
        R.styleable.AmountText_valueTextSize,
        resources.getDimensionPixelOffset(R.dimen.amount_text_value));
    } finally {
      a.recycle();
    }
  }

  public final void setCurrencyTextView(TextView currencyTextView) {
    this.currencyTextView = currencyTextView;
    if (Objects.isNotNull(this.currencyTextView)) {
      this.setCurrencyContent(this.currencyContent);
    }
  }

  public final void setValueTextView(TextView valueTextView) {
    this.valueTextView = valueTextView;
    if (Objects.isNotNull(this.valueTextView)) {
      this.setValueContent(this.valueContent);
    }
  }

  @Override
  public String getCurrencyContent() {
    return currencyContent;
  }

  @Override
  public void setCurrencyContent(String content) {
    currencyContent = content;
    if (Objects.isNotNull(currencyTextView)) {
      currencyTextView.setText(Texts.createContent(
        context,
        currencyContent,
        currencyFontPath,
        currencyTextColor,
        currencyTextSize));
    }
  }

  @Override
  public String getValueContent() {
    return valueContent;
  }

  @Override
  public void setValueContent(String content) {
    valueContent = content;
    if (Objects.isNotNull(valueTextView)) {
      valueTextView.setText(Texts.createContent(
        context,
        valueContent,
        valueFontPath,
        valueTextColor,
        valueTextSize));
    }
  }
}
