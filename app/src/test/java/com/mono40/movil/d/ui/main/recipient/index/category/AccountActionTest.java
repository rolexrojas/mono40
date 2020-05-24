package com.mono40.movil.d.ui.main.recipient.index.category;

import org.junit.Test;

import static com.mono40.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_PHONE_NUMBER;
import static com.mono40.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_ACCOUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link AccountAction} class.
 *
 * @author hecvasro
 */
public final class AccountActionTest {

  private final Action.Type validType = TRANSACTION_WITH_ACCOUNT;
  private final String validString = "123456789";

  @Test
  public final void isProductNumber_nullString_shouldReturnFalse() {
    assertFalse(AccountAction.isProductNumber(null));
  }

  @Test
  public final void isProductNumber_invalidString_shouldReturnFalse() {
    assertFalse(AccountAction.isProductNumber(""));
    assertFalse(AccountAction.isProductNumber("12"));
    assertFalse(AccountAction.isProductNumber("12A"));
  }

  @Test
  public final void isProductNumber_validString_shouldReturnTrue() {
    assertTrue(AccountAction.isProductNumber(this.validString));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_nullString_shouldThrowIllegalArgumentException() {
    AccountAction.create(this.validType, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void create_invalidString_shouldThrowIllegalArgumentException() {
    AccountAction.create(this.validType, "");
  }

  @Test
  public final void create_validTypeAndString_shouldReturnInstance() {
    final AccountAction accountAction = AccountAction.create(this.validType, this.validString);
    assertEquals(this.validType, accountAction.type());
    assertEquals(this.validString, accountAction.number());
  }
}
