package com.mono40.movil.lib;

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
  public final void isValid_onlyDigits_shouldReturnFalse() {
    assertFalse(Password.isValid("012345678"));
  }

  @Test
  public final void isValid_onlyLetters_shouldReturnFalse() {
    assertFalse(Password.isValid("abcdefghi"));
  }

  @Test
  public final void isValid_lessThanEightCharacters_shouldReturnFalse() {
    assertFalse(Password.isValid("01234ab"));
  }

  @Test
  public final void isValid_validArgument_shouldReturnTrue() {
    assertTrue(Password.isValid("a01234567"));
    assertTrue(Password.isValid("abcdefgh0"));
  }
}
