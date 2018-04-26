package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.ListCards;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.tpago.movil.product.Product;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.annotations.Nullable;

final class TestCommonPurchaseUtils {

  static final String DEFAULT_ALT_PAN = "4321312424311234";

  static ListCards createListCards(@Nullable Product product) {
    final ListCards cards = new ListCards();
    if (ObjectHelper.isNotNull(product)) {
      final Card card = new Card();
      card.setAlias(Cub3PosServiceUtils.productAlias(product));
      card.setAltpan(DEFAULT_ALT_PAN);
      card.setArt("art");
      card.setSuffix("suffix");
      cards.addCard(card);
    }
    return cards;
  }

  static ListCards createListCards() {
    return createListCards(null);
  }

  static PaymentInfo createPaymentInfo() {
    final PaymentInfo instance = new PaymentInfo();
    instance.setValue("value");
    instance.setDate("date");
    instance.setReference("reference");
    instance.setTime("time");
    instance.setCrypto("crypto");
    instance.setAtc("atc");
    return instance;
  }

  private TestCommonPurchaseUtils() {
  }
}
