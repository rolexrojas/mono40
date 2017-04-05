package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Pin {
  private static final String MASK = "•";

  private static final int LENGTH = 4;

  private static Pin create(List<Digit> digits) {
    return new AutoValue_Pin(Digits.stringify(digits));
  }

  public abstract String getValue();

  public static final class Builder {
    private final List<Digit> digits;

    public Builder() {
      digits = new ArrayList<>();
    }

    public final String getMaskedValue() {
      final StringBuilder builder = new StringBuilder();
      for (Digit d : digits) {
        builder.append(MASK);
      }
      return builder.toString();
    }

    public final boolean canBuild() {
      return digits.size() == LENGTH;
    }

    public final Builder addDigit(Digit digit) {
      Preconditions.assertNotNull(digit, "digit == null");
      if (!canBuild()) {
        digits.add(digit);
      }
      return this;
    }

    public final Builder removeLastDigit() {
      final int size = digits.size();
      if (size > 0) {
        digits.remove(size - 1);
      }
      return this;
    }

    public final Pin build() {
      if (!canBuild()) {
        throw new IllegalStateException("canBuild() == false");
      }
      return Pin.create(digits);
    }
  }
}
