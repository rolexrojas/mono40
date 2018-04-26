package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.google.common.base.MoreObjects;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of helper methods related to Cub3's {@link PosService} implementation
 */
final class Cub3PosServiceUtils {

  /**
   * Obtains the identifier of an {@link User user}.
   *
   * @return Identifier of an {@link User user}.
   *
   * @throws NullPointerException
   *   If {@code user} is {@code null}.
   */
  static String userIdentifier(User user) {
    return ObjectHelper.checkNotNull(user, "user")
      .phoneNumber()
      .value();
  }

  /**
   * Obtains the alias of a {@link Product product}.
   *
   * @return Alias of a {@link Product product}.
   *
   * @throws NullPointerException
   *   If {@code product} is {@code null}.
   */
  static String productAlias(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    final String number = product.number();
    final int numberLength = number.length();
    return number.substring(numberLength - 4, numberLength);
  }

  /**
   * Obtains the message of a {@link CubeError error}.
   * <p>
   * The message will be the concatenation of {@link CubeError#getErrorMessage()} and {@link
   * CubeError#getErrorDetails()}, separated by ': '.
   *
   * @return Message of a {@link CubeError error}.
   *
   * @throws NullPointerException
   *   If {@code error} is {@code null}.
   */
  static String errorMessage(CubeError error) {
    ObjectHelper.checkNotNull(error, "error");
    return String.format("%1$s: %2$s", error.getErrorMessage(), error.getErrorDetails());
  }

  /**
   * Maps an {@link CubeError error} to an unsuccessful {@link Result result}.
   *
   * @return Unsuccessful {@link Result result} created from the given {@link CubeError error}.
   *
   * @throws NullPointerException
   *   If {@code error} is {@code null}.
   */
  static <T> Result<T> mapToResult(CubeError error) {
    ObjectHelper.checkNotNull(error, "error");
    final FailureData failureData = FailureData.builder()
      .code(Integer.parseInt(error.getErrorCode()))
      .description(errorMessage(error))
      .build();
    return Result.create(failureData);
  }

  /**
   * Transforms a given instance of {@link ListCards} to into a {@link String}.
   *
   * @return {@link String} representation of the given {@link ListCards} instance.
   */
  static String mapToString(ListCards instance) {
    ObjectHelper.checkNotNull(instance, "instance");
    final List<String> cardStrings = new ArrayList<>();
    String cardString;
    for (Card card : instance.getCards()) {
      cardString = MoreObjects.toStringHelper(card)
        .add("alias", card.getAlias())
        .add("altPan", card.getAltpan())
        .add("suffix", card.getSuffix())
        .add("art", card.getArt())
        .toString();
      cardStrings.add(cardString);
    }
    return MoreObjects.toStringHelper(instance)
      .add("cards", cardStrings)
      .toString();
  }

  /**
   * Transforms a given instance of {@link PaymentInfo} into a {@link String}.
   *
   * @return {@link String} representation of the given {@link PaymentInfo} instance.
   */
  static String mapToString(PaymentInfo instance) {
    ObjectHelper.checkNotNull(instance, "instance");
    return MoreObjects.toStringHelper(instance)
      .add("value", instance.getValue())
      .add("date", instance.getDate())
      .add("reference", instance.getReference())
      .add("time", instance.getTime())
      .add("crypto", instance.getCrypto())
      .add("atc", instance.getAtc())
      .toString();
  }

  private Cub3PosServiceUtils() {
  }
}
