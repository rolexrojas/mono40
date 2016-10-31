package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.data.Formatter;
import com.gbh.movil.ui.UiUtils;

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
      defStyleAttr, R.style.Widget_AmountView);
    try {
      currency = array.getString(R.styleable.AmountView_currency);
      value = array.getFloat(R.styleable.AmountView_value, 0F);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.amount_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    setCurrency(currency);
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
  public double getValue() {
    return value;
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   */
  public void setValue(double value) {
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
