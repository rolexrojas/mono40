package com.tpago.movil.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public final class AmountTextView extends LinearLayout implements AmountText {
  private AmountTextHelper amountTextHelper;

  @BindView(R.id.text_view_currency) TextView currencyTextView;
  @BindView(R.id.text_view_value) TextView valueTextView;

  public AmountTextView(Context context) {
    super(context);
    initializeAmountTextView(context, null, R.attr.amountTextViewStyle);
  }

  public AmountTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAmountTextView(context, attrs, R.attr.amountTextViewStyle);
  }

  public AmountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeAmountTextView(context, attrs, defStyleAttr);
  }

  private void initializeAmountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    setOrientation(HORIZONTAL);
    amountTextHelper = new AmountTextHelper(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.widget_text_view_amount, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    // Binds all the annotated resources, views and methods.
    ButterKnife.bind(this);
    // Sets the currency and value text views of the amount text helper.
    amountTextHelper.setCurrencyTextView(currencyTextView);
    amountTextHelper.setValueTextView(valueTextView);
  }

  @Override
  public String getCurrencyContent() {
    return amountTextHelper.getCurrencyContent();
  }

  @Override
  public void setCurrencyContent(String content) {
    amountTextHelper.setCurrencyContent(content);
  }

  @Override
  public String getValueContent() {
    return amountTextHelper.getValueContent();
  }

  @Override
  public void setValueContent(String content) {
    amountTextHelper.setValueContent(content);
  }
}
