package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.data.Formatter;
import com.gbh.movil.ui.UiUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AmountView extends LinearLayout {
  /**
   * TODO
   */
  @StyleRes
  private int currencyTextAppearance;
  /**
   * TODO
   */
  private String currency;
  /**
   * TODO
   */
  @StyleRes
  private int valueTextAppearance;
  /**
   * TODO
   */
  private BigDecimal value;

  @BindView(R.id.currency)
  TextView currencyTextView;
  @BindView(R.id.value)
  TextView valueTextView;

  public AmountView(Context context) {
    this(context, null);
  }

  public AmountView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.amountViewStyle);
  }

  public AmountView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(LinearLayout.HORIZONTAL);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AmountView,
      defStyleAttr, R.style.App_Widget_AmountView);
    try {
      currencyTextAppearance = array.getResourceId(R.styleable.AmountView_currencyTextAppearance,
        R.style.App_Text_Widget_AmountView_Currency);
      currency = array.getString(R.styleable.AmountView_currency);
      valueTextAppearance = array.getResourceId(R.styleable.AmountView_valueTextAppearance,
        R.style.App_Text_Widget_AmountView_Value);
      value = BigDecimal.valueOf(array.getFloat(R.styleable.AmountView_value, 0F));
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.widget_amount_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    UiUtils.setTextAppearance(currencyTextView, currencyTextAppearance);
    setCurrency(currency);
    UiUtils.setTextAppearance(valueTextView, valueTextAppearance);
    setValue(value);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public String getCurrency() {
    return currency;
  }

  /**
   * TODO
   *
   * @param currency
   *   TODO
   */
  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
    this.currencyTextView.setText(this.currency);
    this.currencyTextView.setVisibility(this.currency != null ? View.VISIBLE : View.GONE);
  }

  /**
   * TODO
   *
   * @param colorId
   *   TODO
   */
  public void setCurrencyColor(@ColorRes int colorId) {
    currencyTextView.setTextColor(UiUtils.getColor(getContext(), colorId));
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public BigDecimal getValue() {
    return value;
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   */
  public void setValue(@NonNull BigDecimal value) {
    this.value = value;
    this.valueTextView.setText(Formatter.amount(this.value));
  }

  /**
   * TODO
   *
   * @param colorId
   *   TODO
   */
  public void setValueColor(@ColorRes int colorId) {
    valueTextView.setTextColor(UiUtils.getColor(getContext(), colorId));
  }
}
