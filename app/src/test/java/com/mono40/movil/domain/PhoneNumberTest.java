package com.mono40.movil.domain;

import com.mono40.movil.PhoneNumber;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Test cases for the {@link PhoneNumber} class.
 *
 * @author hecvasro
 */
public final class PhoneNumberTest {

  @Test
  public final void isValid_nullString_shouldReturnFalse() {
    assertFalse(PhoneNumber.isValid(null));
  }

  @Test
  public final void isValid_invalidString_shouldReturnFalse() {
    assertFalse(PhoneNumber.isValid(""));
    assertFalse(PhoneNumber.isValid("hecvasro"));
    assertFalse(PhoneNumber.isValid("012-345-6789"));
    assertFalse(PhoneNumber.isValid("8194125947"));
  }

  @Test
  public final void isValid_validString_shouldReturnTrue() {
    assertTrue(PhoneNumber.isValid("8094125947"));
    assertTrue(PhoneNumber.isValid("809-412-5947"));
    assertTrue(PhoneNumber.isValid("8294125947"));
    assertTrue(PhoneNumber.isValid("829-412-5947"));
    assertTrue(PhoneNumber.isValid("8494125947"));
    assertTrue(PhoneNumber.isValid("849-412-5947"));
  }

  @Test
  public final void format() {
    assertEquals("809", PhoneNumber.format("809"));
    assertEquals("809-412", PhoneNumber.format("809412"));
    assertEquals("809-412-5947", PhoneNumber.format("8094125947"));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_nullString_shouldThrowIllegalArgumentException() {
    PhoneNumber.create(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_invalidString_shouldThrowIllegalArgumentException() {
    PhoneNumber.create("012-345-6789");
  }

  @Test
  public final void create_validString() {
    final PhoneNumber phoneNumber = PhoneNumber.create("8094125947");
    assertEquals("8094125947", phoneNumber.value());
    assertEquals("809-412-5947", phoneNumber.formattedValued());
  }

  @Test
  public final void compareTo() {
    final PhoneNumber phoneNumberA = PhoneNumber.create("8094125947");
    final PhoneNumber phoneNumberB = PhoneNumber.create("8294125947");
    assertTrue(phoneNumberA.compareTo(phoneNumberB) < 0);
    assertTrue(phoneNumberA.compareTo(phoneNumberA) == 0);
    assertTrue(phoneNumberB.compareTo(phoneNumberA) > 0);
  }
}
