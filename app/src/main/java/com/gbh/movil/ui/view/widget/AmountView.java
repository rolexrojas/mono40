package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.data.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AmountView extends LinearLayout {
  private String currency;
  private double value;

  @StyleRes
  private int currencyTextAppearance;
  @StyleRes
  private int valueTextAppearance;

  @BindView(R.id.text_view_currency)
  TextView currencyTextView;
  @BindView(R.id.text_view_value)
  TextView valueTextView;

  public AmountView(Context context) {
    this(context, null);
  }

  public AmountView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.amountStyle);
  }

  public AmountView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(LinearLayout.HORIZONTAL);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AmountView,
      defStyleAttr, R.style.Amount);
    try {
      currencyTextAppearance = array.getResourceId(R.styleable.AmountView_currencyTextAppearance,
        R.style.Text_Amount_Currency_Normal);
      currency = array.getString(R.styleable.AmountView_currency);
      valueTextAppearance = array.getResourceId(R.styleable.AmountView_valueTextAppearance,
        R.style.Text_Amount_Value_Normal);
      value = array.getFloat(R.styleable.AmountView_value, 0F);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.amount, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    TextViewCompat.setTextAppearance(currencyTextView, currencyTextAppearance);
    TextViewCompat.setTextAppearance(valueTextView, valueTextAppearance);
    setCurrency(currency);
    setValue(value);
  }

  @Nullable
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
    this.currencyTextView.setText(this.currency);
    this.currencyTextView.setVisibility(this.currency != null ? View.VISIBLE : View.GONE);
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
    this.valueTextView.setText(Formatter.amount(this.value));
  }

  public void setValueColor(@ColorRes int colorId) {
    valueTextView.setTextColor(ContextCompat.getColor(getContext(), colorId));
  }
}
