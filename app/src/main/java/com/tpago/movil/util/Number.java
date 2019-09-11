package com.tpago.movil.util;

import androidx.annotation.Nullable;

import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.digit.DigitValueCreator;
import com.tpago.movil.util.digit.DigitValueCreatorImpl;

/**
 * @author hecvasro
 */
public final class Number {

  public static Creator creator(@Nullable Integer placeholder) {
    return new Creator(placeholder);
  }

  public static Creator creator() {
    return creator(null);
  }

  private Number() {
  }

  public static final class Creator implements DigitValueCreator<Integer> {

    private final Integer placeholder;
    private final DigitValueCreator<Integer> creator;

    private Creator(@Nullable Integer placeholder) {
      this.placeholder = placeholder;
      this.creator = DigitValueCreatorImpl.<Integer>builder()
        .canAdd((l) -> true)
        .isValid((s) -> !s.isEmpty())
        .mapper(Integer::parseInt)
        .build();
    }

    @Override
    public void clear() {
      this.creator.clear();
    }

    @Override
    public void addDigit(@Digit int digit) {
      this.creator.addDigit(digit);
    }

    @Override
    public void removeLastDigit() {
      this.creator.removeLastDigit();
    }

    @Override
    public boolean canCreate() {
      return true;
    }

    @Override
    public Integer create() {
      if (!this.creator.canCreate()) {
        return this.placeholder;
      }
      return this.creator.create();
    }

    @Override
    public String toString() {
      if (!this.creator.canCreate()) {
        return Integer.toString(this.placeholder);
      }
      return this.creator.toString();
    }
  }
}
