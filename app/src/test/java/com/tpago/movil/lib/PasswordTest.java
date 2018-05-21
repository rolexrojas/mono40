package com.tpago.movil.lib;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link Password} class.
 */
public final class PasswordTest {

  @Test
  public final void isValid_null_shouldReturnFalse() {
    assertFalse(Password.isValid(null));
  }

  @Test
  public final void isValid_empty_shouldReturnFalse() {
    assertFalse(Password.isValid(""));
  }

  @Test
  public final void isValid_lessThanEightCharacters_shouldReturnFalse() {
    assertFalse(Password.isValid("01234ab"));
  }

  @Test
  public final void isValid_onlyDigits_shouldReturnTrue() {
    assertTrue(Password.isValid("012345678"));
  }

  @Test
  public final void isValid_onlyLetters_shouldReturnTrue() {
    assertTrue(Password.isValid("abcdefghi"));
  }

  @Test
  public final void isValid_digitsAndLetters_shouldReturnTrue() {
    assertTrue(Password.isValid("a0123456b"));
  }
}
