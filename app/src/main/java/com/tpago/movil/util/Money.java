package com.tpago.movil.util;

import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.digit.DigitValueCreator;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author hecvasro
 */
public final class Money {

  public static String format(BigDecimal value) {
    return String.format(Locale.ENGLISH, "%1$,.2f", ObjectHelper.checkNotNull(value, "value"));
  }

  public static String format(String currency, BigDecimal value) {
    StringHelper.checkIsNotNullNorEmpty(currency, "currency");
    ObjectHelper.checkNotNull(value, "value");
    return StringHelper.builder()
      .append(currency)
      .append(format(value))
      .toString();
  }

  public static Creator creator() {
    return new Creator();
  }

  private Money() {
  }

  public static final class Creator implements DigitValueCreator<BigDecimal> {

    private final Number.Creator whole;
    private final Number.Creator fractional;

    private boolean shouldAddAsFractional = false;
    private BigDecimal value = BigDecimal.ZERO;

    private Creator() {
      this.whole = Number.creator(0);
      this.fractional = Number.creator(0);
    }

    public final void switchToFractional() {
      if (!this.shouldAddAsFractional) {
        this.shouldAddAsFractional = true;
        this.updateValue();
      }
    }

    private void updateValue() {
      final String s = StringHelper.builder()
        .append(this.whole.create())
        .append('.')
        .append(this.fractional.create())
        .toString();
      this.value = new BigDecimal(s);
    }

    @Override
    public void clear() {
      this.whole.clear();
      this.fractional.clear();
      this.shouldAddAsFractional = false;
      this.updateValue();
    }

    @Override
    public void addDigit(@Digit int digit) {
      if (this.shouldAddAsFractional) {
        this.fractional.addDigit(digit);
      } else {
        if(this.whole.toString().length() < 8){
          this.whole.addDigit(digit);
        }
      }
      this.updateValue();
    }

    @Override
    public void removeLastDigit() {
      if (this.shouldAddAsFractional) {
        this.fractional.removeLastDigit();
        if (!this.fractional.canCreate()) {
          this.shouldAddAsFractional = false;
        }
      } else {
        this.whole.removeLastDigit();
      }
      this.updateValue();
    }

    @Override
    public boolean canCreate() {
      return true;
    }

    @Override
    public BigDecimal create() {
      return this.value;
    }

    @Override
    public String toString() {
      return format(this.value);
    }
  }
}
