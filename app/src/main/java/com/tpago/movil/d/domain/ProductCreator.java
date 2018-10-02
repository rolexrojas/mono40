package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.company.bank.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
public final class ProductCreator {

  @NonNull
  public static Product create(
    ProductType identifier,
    String alias,
    String number,
    Bank bank,
    String currency,
    BigDecimal queryFee,
    boolean paymentOption,
    boolean isDefault,
    String imageUrl,
    String altpanKey
    ) {
    final Product p = new Product(
      identifier,
      alias,
      number,
      bank,
      currency,
      queryFee,
      paymentOption,
      isDefault,
      altpanKey
    );
    p.setImageUriTemplate(imageUrl);
    return p;
  }
}
