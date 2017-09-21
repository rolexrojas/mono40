package com.tpago.movil.d.ui.main.recipient.index.category;

import org.junit.Test;

import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_PHONE_NUMBER;
import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_PRODUCT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link ProductAction} class.
 *
 * @author hecvasro
 */
public final class ProductActionTest {

  private final Action.Type validType = TRANSACTION_WITH_PRODUCT;
  private final String validString = "123456789";

  @Test
  public final void isProductNumber_nullString_shouldReturnFalse() {
    assertFalse(ProductAction.isProductNumber(null));
  }

  @Test
  public final void isProductNumber_invalidString_shouldReturnFalse() {
    assertFalse(ProductAction.isProductNumber(""));
    assertFalse(ProductAction.isProductNumber("12"));
    assertFalse(ProductAction.isProductNumber("12A"));
  }

  @Test
  public final void isProductNumber_validString_shouldReturnTrue() {
    assertTrue(ProductAction.isProductNumber(this.validString));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_invalidType_shouldThrownIllegalArgumentException() {
    ProductAction.create(TRANSACTION_WITH_PHONE_NUMBER, this.validString);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_nullString_shouldThrowIllegalArgumentException() {
    ProductAction.create(this.validType, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_invalidString_shouldThrowIllegalArgumentException() {
    ProductAction.create(this.validType, "");
  }

  @Test
  public final void create_validTypeAndString_shouldReturnInstance() {
    final ProductAction productAction = ProductAction.create(this.validType, this.validString);
    assertEquals(this.validType, productAction.type());
    assertEquals(this.validString, productAction.number());
  }
}
