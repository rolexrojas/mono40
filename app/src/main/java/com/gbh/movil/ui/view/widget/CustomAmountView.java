package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class CustomAmountView extends AmountView {
  /**
   * TODO
   */
  private static final BigDecimal ZERO = BigDecimal.ZERO;
  /**
   * TODO
   */
  private static final BigDecimal ONE = BigDecimal.ONE;
  /**
   * TODO
   */
  private static final BigDecimal TEN = BigDecimal.TEN;
  /**
   * TODO
   */
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  /**
   * TODO
   */
  private boolean mustShowDot = false;

  /**
   * TODO
   */
  private BigDecimal fractionOffset = ONE;

  public CustomAmountView(Context context) {
    super(context);
  }

  public CustomAmountView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomAmountView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private static BigDecimal getFraction(@NonNull BigDecimal value) {
    return value.subtract(BigDecimal.valueOf(value.intValue()));
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   *
   * @return TODO
   */
  private static boolean isFraction(@NonNull BigDecimal value) {
    return value.compareTo(ZERO) > 0;
  }

  @NonNull
  @Override
  protected String getFormattedValue() {
    String formattedValue = super.getFormattedValue();
    final BigDecimal fraction = getFraction(getValue());
    if (!isFraction(fraction)) {
      formattedValue = formattedValue.replace(".00", "");
      if (mustShowDot) {
        formattedValue += ".";
      }
    } else if (fraction.multiply(fractionOffset).compareTo(TEN) < 0) {
      formattedValue = formattedValue.substring(0, formattedValue.length() - 1);
    }
    return formattedValue;
  }

  /**
   * TODO
   */
  public final void pop() {
    BigDecimal value = getValue();
    final int result = value.compareTo(ZERO);
    if (result > 0) {
      final BigDecimal fraction = getFraction(value);
      if (isFraction(fraction)) {
        value = value.subtract(fraction.multiply(fractionOffset).remainder(TEN)
          .divide(fractionOffset, 2, RoundingMode.CEILING));
        fractionOffset = fractionOffset.divide(TEN, 2, RoundingMode.CEILING);
      } else if (mustShowDot) {
        mustShowDot = false;
      } else {
        value = value.divideToIntegralValue(TEN);
      }
    }
    setValue(value);
  }

  /**
   * TODO
   *
   * @param digit
   *   TODO
   */
  public final void pushDigit(int digit) {
    BigDecimal value = getValue();
    BigDecimal addition = BigDecimal.valueOf(digit);
    if (mustShowDot) {
      if (fractionOffset.compareTo(HUNDRED) < 0) {
        value = value.add(addition.divide(TEN.multiply(fractionOffset), 2, RoundingMode.CEILING));
        fractionOffset = fractionOffset.multiply(TEN);
      }
    } else {
      value = value.multiply(TEN).add(addition);
    }
    setValue(value);
  }

  /**
   * TODO
   */
  public final void pushDot() {
    if (!mustShowDot) {
      mustShowDot = true;
      setValue(getValue());
    }
  }
}
