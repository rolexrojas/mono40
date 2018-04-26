package com.tpago.movil.d.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases of the {@link ProductTest} class
 */
public final class ProductTest {

  @Test
  public final void numberMasked() {
    final String number = "4242";
    assertEquals("•••• 4242", Product.numberMasked(number, 1));
    assertEquals("•••• •••• 4242", Product.numberMasked(number, 2));
    assertEquals("•••• •••• •••• 4242", Product.numberMasked(number, 3));
  }
}
