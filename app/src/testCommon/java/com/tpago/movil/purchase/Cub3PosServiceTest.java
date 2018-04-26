package com.tpago.movil.purchase;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.Code;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases for the {@link Cub3PosService} class
 */
public final class Cub3PosServiceTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Mock User user;
  @Mock Product product;
  @Mock CubeSdkImpl sdk;

  private final String phoneNumberValue = "8094204200";
  private final PhoneNumber phoneNumber = PhoneNumber.create(this.phoneNumberValue);

  private final String productNumber = "************4242";
  private final String productAlias = "4242";

  private final String pinValue = "4242";
  private final Code pin = Code.create(this.pinValue);

  private Cub3PosService service;

  @Before
  public final void setUp() {
    this.service = Cub3PosService.builder()
      .sdk(() -> this.sdk)
      .build();
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullUserWhenRegisteringProductThenExceptionIsThrown() {
    this.service.registerProduct(null, this.product, this.pin);
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullProductWhenRegisteringProductThenExceptionIsThrown() {
    this.service.registerProduct(this.user, null, this.pin);
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullPinWhenRegisteringProductThenExceptionIsThrown() {
    this.service.registerProduct(this.user, this.product, null);
  }

  @Test
  public final void givenRegisteredProductWhenRegisteringProductThenProductIsNotRegistered() {
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards(this.product));
      return null;
    })
      .when(this.sdk)
      .ListCard(any());

    this.service.registerProduct(this.user, this.product, this.pin)
      .test()
      .assertValue((result) -> result.equals(Result.create(Placeholder.get())));

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk, never())
      .AddCard(any(), any());
  }

  @Test
  public final void givenUnregisteredProductWhenRegisteringProductThenProductIsRegistered() {
    when(this.user.phoneNumber())
      .thenReturn(this.phoneNumber);
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards());
      return null;
    })
      .when(this.sdk)
      .ListCard(any());
    doAnswer((invocation) -> {
      final AddCardParams params = invocation.getArgument(0);
      assertEquals(this.phoneNumberValue, params.getMsisdn());
      assertEquals(this.productAlias, params.getAlias());
      assertEquals(this.pinValue, params.getOtp());

      invocation.<CubeSdkCallback<String, CubeError>>getArgument(1)
        .success("success");
      return null;
    })
      .when(this.sdk)
      .AddCard(any(), any());

    this.service.registerProduct(this.user, this.product, this.pin)
      .test()
      .assertValue((result) -> result.equals(Result.create(Placeholder.get())));

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk)
      .AddCard(any(), any());
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullProductWhenUnregisteringProductThenExceptionIsThrown() {
    this.service.unregisterProduct(null);
  }

  @Test
  public final void givenUnregisteredProductWhenUnregisteringProductThenProductIsNotUnregistered() {
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards());
      return null;
    })
      .when(this.sdk)
      .ListCard(any());

    this.service.unregisterProduct(this.product)
      .test()
      .assertComplete();

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk, never())
      .DeleteCard(any(), any());
  }

  @Test
  public final void givenRegisteredProductWhenUnregisteringProductThenProductIsUnregistered() {
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards(this.product));
      return null;
    })
      .when(this.sdk)
      .ListCard(any());
    doAnswer((invocation) -> {
      final SelectCardParams params = invocation.getArgument(0);
      assertEquals(Cub3PosServiceUtils.productAlias(this.product), params.getAlias());
      assertEquals(TestCommonPurchaseUtils.DEFAULT_ALT_PAN, params.getAltpan());

      invocation.<CubeSdkCallback<String, CubeError>>getArgument(1)
        .success("success");
      return null;
    })
      .when(this.sdk)
      .DeleteCard(any(), any());

    this.service.unregisterProduct(this.product)
      .test()
      .assertComplete();

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk)
      .DeleteCard(any(), any());
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullUserWhenUnregisteringAllProductsThenExceptionIsThrown() {
    this.service.unregisterAllProducts(null);
  }

  @Test
  public final void givenNoProductsRegisteredWhenUnregisteringAllProductsThenProductsAreNotUnregistered() {
    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards());
      return null;
    })
      .when(this.sdk)
      .ListCard(any());

    this.service.unregisterAllProducts(this.user)
      .test()
      .assertComplete();

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk, never())
      .Unregister(eq(this.phoneNumberValue), any());
  }

  @Test
  public final void givenProductsRegisteredWhenUnregisteringAllProductsThenProductsAreUnregistered() {
    when(this.user.phoneNumber())
      .thenReturn(this.phoneNumber);
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards(this.product));
      return null;
    })
      .when(this.sdk)
      .ListCard(any());
    doAnswer((invocation) -> {
      assertEquals(this.phoneNumberValue, invocation.<String>getArgument(0));

      invocation.<CubeSdkCallback<String, CubeError>>getArgument(1)
        .success("success");
      return null;
    })
      .when(this.sdk)
      .Unregister(eq(this.phoneNumberValue), any());

    this.service.unregisterAllProducts(this.user)
      .test()
      .assertComplete();

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk)
      .Unregister(eq(this.phoneNumberValue), any());
  }

  @Test(expected = NullPointerException.class)
  public final void givenNullProductWhenStartingPurchaseThenExceptionIsThrown() {
    this.service.unregisterAllProducts(null);
  }

  @Test
  public final void givenUnregisteredProductWhenStartingPurchaseThenExceptionIsThrown() {
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards());
      return null;
    })
      .when(this.sdk)
      .ListCard(any());

    this.service.startPurchase(this.product)
      .test()
      .assertError(IllegalArgumentException.class);

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk, never())
      .SelectCard(any(), any());
  }

  @Test
  public final void givenRegisteredProductWhenStartingPurchaseThenPurchaseIsCompleted() {
    when(this.product.number())
      .thenReturn(this.productNumber);

    doAnswer((invocation) -> {
      invocation.<CubeSdkCallback<ListCards, CubeError>>getArgument(0)
        .success(TestCommonPurchaseUtils.createListCards(this.product));
      return null;
    })
      .when(this.sdk)
      .ListCard(any());
    doAnswer((invocation) -> {
      final SelectCardParams params = invocation.getArgument(0);
      assertEquals(this.productAlias, params.getAlias());
      assertEquals(TestCommonPurchaseUtils.DEFAULT_ALT_PAN, params.getAltpan());

      invocation.<CubeSdkCallback<PaymentInfo, CubeError>>getArgument(1)
        .success(TestCommonPurchaseUtils.createPaymentInfo());
      return null;
    })
      .when(this.sdk)
      .SelectCard(any(), any());

    this.service.startPurchase(this.product)
      .test()
      .assertComplete();

    verify(this.sdk)
      .ListCard(any());
    verify(this.sdk)
      .SelectCard(any(), any());
  }
}
