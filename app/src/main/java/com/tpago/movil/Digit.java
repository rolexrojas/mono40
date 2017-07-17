package com.tpago.movil;

/**
 * @author hecvasro
 */
public enum Digit {
  ZERO(0),
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9);

  private final int value;

  public static Digit find(int value) {
    if (value < 0 || value > 9) {
      throw new IllegalArgumentException("value < 0 || value > 9");
    }
    Digit digit = null;
    for (Digit current : values()) {
      if (current.getValue() == value) {
        digit = current;
      }
    }
    return digit;
  }

  Digit(int value) {
    this.value = value;
  }

  public final int getValue() {
    return value;
  }
}
