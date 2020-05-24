package com.mono40.movil.util.digit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link DigitUtil} class.
 */
public final class DigitUtilTest {

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_nullAsArgument_shouldThrowIllegalArgumentException() {
    DigitUtil.getLast4Digits(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_emptyStringAsArgument_shouldThrowIllegalArgumentException() {
    DigitUtil.getLast4Digits("");
  }

  @Test(expected = IllegalArgumentException.class)
  public final void getLast4Digits_stringWithLessThanFourDigits_shouldThrowIllegalArgumentException() {
    DigitUtil.getLast4Digits("123");
  }

  @Test
  public final void getLast4Digits_stringWithFourOrMoreDigits_shouldReturnLast4Digits() {
    Assert.assertEquals("1234", DigitUtil.getLast4Digits("************1234"));
    Assert.assertEquals("1234", DigitUtil.getLast4Digits("0000********1234"));
  }
}
