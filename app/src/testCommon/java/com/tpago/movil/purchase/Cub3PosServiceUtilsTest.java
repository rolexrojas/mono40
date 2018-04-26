package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.CubeError;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Result;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases of the {@link Cub3PosServiceUtils} class
 */
public final class Cub3PosServiceUtilsTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Mock User user;
  @Mock Product product;

  @Test(expected = NullPointerException.class)
  public final void givenNullUserWhenObtainingItsIdentifierThenExceptionIsThrown() {
    Cub3PosServiceUtils.userIdentifier(this.user);
  }

  @Test
  public final void givenUserWhenObtainingItsIdentifierThenItsIdentifierIsReturned() {
    final String phoneNumberString = "8094204200";
    final PhoneNumber phoneNumber = PhoneNumber.create(phoneNumberString);
    when(this.user.phoneNumber())
      .thenReturn(phoneNumber);
    assertEquals(phoneNumberString, Cub3PosServiceUtils.userIdentifier(this.user));
    verify(this.user)
      .phoneNumber();
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullProductWhenObtainingItsAliasThenExceptionIsThrown() {
    Cub3PosServiceUtils.productAlias(this.product);
  }

  @Test
  public final void givenProductWhenObtainingItsAliasThenItsAliasIsReturned() {
    String number;

    number = "************4242";
    when(this.product.number())
      .thenReturn(number);
    assertEquals("4242", Cub3PosServiceUtils.productAlias(this.product));

    number = "4321********1234";
    when(this.product.number())
      .thenReturn(number);
    assertEquals("1234", Cub3PosServiceUtils.productAlias(this.product));

    verify(this.product, times(2))
      .number();
  }

  private static CubeError createError() {
    final CubeError error = new CubeError();
    error.setErrorCode("42");
    error.setErrorMessage("Unexpected error");
    error.setErrorDetails("An unexpected error has occurred!");
    return error;
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullErrorWhenObtainingItsMessageThenExceptionIsThrown() {
    Cub3PosServiceUtils.errorMessage(null);
  }

  @Test
  public final void givenErrorWhenObtainingItsMessageThenItsMessageIsReturned() {
    final CubeError error = createError();
    final String expectedMessage = String
      .format("%1$s: %2$s", error.getErrorMessage(), error.getErrorDetails());
    assertEquals(expectedMessage, Cub3PosServiceUtils.errorMessage(error));
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullErrorWhenMappingToResultThenExceptionIsThrown() {
    Cub3PosServiceUtils.mapToResult(null);
  }

  @Test
  public final void givenErrorWhenMappingToResultThenErrorIsMapped() {
    final CubeError error = createError();
    final FailureData failureData = FailureData.builder()
      .code(Integer.parseInt(error.getErrorCode()))
      .description(String.format("%1$s: %2$s", error.getErrorMessage(), error.getErrorDetails()))
      .build();
    assertEquals(Result.create(failureData), Cub3PosServiceUtils.mapToResult(error));
  }
}
