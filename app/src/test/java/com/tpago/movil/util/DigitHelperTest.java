package com.tpago.movil.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link DigitHelper} class.
 */
public final class DigitHelperTest {

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_nullAsArgument_shouldThrowIllegalArgumentException() {
    DigitHelper.getLast4Digits(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_emptyStringAsArgument_shouldThrowIllegalArgumentException() {
    DigitHelper.getLast4Digits("");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_stringWithLessThanFourDigits_shouldThrowIllegalArgumentException() {
    DigitHelper.getLast4Digits("123");
  }

  @Test
  public final void getLast4Digits_stringWithFourOrMoreDigits_shouldReturnLast4Digits() {
    Assert.assertEquals("1234", DigitHelper.getLast4Digits("************1234"));
    Assert.assertEquals("1234", DigitHelper.getLast4Digits("0000********1234"));
  }
}
