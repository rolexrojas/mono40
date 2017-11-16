package com.tpago.movil.app.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.tpago.movil.R;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 * @see <a href="https://medium.com/@ali.muzaffar/adding-a-prefix-to-an-edittext-2a17a62c77e1">https://medium.com/@ali.muzaffar/adding-a-prefix-to-an-edittext-2a17a62c77e1</a>
 */
public final class MoneyLabel extends AppCompatTextView {

  private float leftPadding = 0F;

  private String currency;
  private int currencyWidth;
  private TextPaint currencyPaint;

  public MoneyLabel(Context context, AttributeSet attrs) {
    super(context, attrs);

    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MoneyLabel);
    this.currency = array.getString(R.styleable.MoneyLabel_currency);
    array.recycle();

    this.initializeCurrencyPaint();
  }

  private void calculateCurrencyWidth() {
    this.currencyWidth = 0;
    if (!StringHelper.isNullOrEmpty(this.currency)) {
      final float[] ws = new float[this.currency.length()];
      this.currencyPaint.getTextWidths(this.currency, ws);
      for (float w : ws) {
        this.currencyWidth += w;
      }
    }
  }

  private void initializeCurrencyPaint() {
    // TODO: Set baseline shift.

    this.calculateCurrencyWidth();
  }

  public final void setCurrency(String currency) {
    this.currency = StringHelper.nullIfEmpty(currency);

    this.calculateCurrencyWidth();

    if (!this.isInLayout()) {
      this.requestLayout();
    }
  }

  @Nullable
  public final String getCurrency() {
    return this.currency;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    this.setPadding(
      this.currencyWidth,
      this.getPaddingTop(),
      this.getPaddingRight(),
      this.getPaddingBottom()
    );
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    final TextPaint currencyPaint = new TextPaint(this.getPaint());
    currencyPaint.setAlpha(128); // 50%
    currencyPaint.setTextSize(this.getTextSize() * 0.0625F);

//    currencyPaint.setColor(this.getCurrentTextColor());
//    currencyPaint.drawableState = this.getDrawableState();

    canvas.drawText(
      this.currency,
      this.leftPadding,
      this.getLineBounds(0, null),
      currencyPaint
    );
  }
}
