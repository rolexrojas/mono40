package com.tpago.movil.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Digit enumeration
 *
 * @author hecvasro
 */
@IntDef({
  Digit.ZERO,
  Digit.ONE,
  Digit.TWO,
  Digit.THREE,
  Digit.FOUR,
  Digit.FIVE,
  Digit.SIX,
  Digit.SEVEN,
  Digit.EIGHT,
  Digit.NINE,
})
@Retention(SOURCE)
public @interface Digit {

  int ZERO = 0;
  int ONE = 1;
  int TWO = 2;
  int THREE = 3;
  int FOUR = 4;
  int FIVE = 5;
  int SIX = 6;
  int SEVEN = 7;
  int EIGHT = 8;
  int NINE = 9;
}
