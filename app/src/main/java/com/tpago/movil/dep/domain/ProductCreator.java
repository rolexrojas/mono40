package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
public final class ProductCreator {
  @NonNull
  public static Product create(
    @NonNull ProductType identifier,
    @NonNull String alias,
    @NonNull String number,
    @NonNull Bank bank,
    @NonNull String currency,
    @NonNull BigDecimal queryFee,
    boolean paymentOption,
    boolean isDefault,
    String imageUrl) {
    final Product p;
    if (identifier.equals(ProductType.LOAN)) {
      p = new Loan(identifier, alias, number, bank, currency, queryFee, paymentOption, isDefault);
    } else if (identifier.equals(ProductType.AMEX) || identifier.equals(ProductType.CC) || identifier.equals(ProductType.TBD)) {
      p = new CreditCard(identifier, alias, number, bank, currency, queryFee, paymentOption, isDefault);
    } else {
      p = new Account(identifier, alias, number, bank, currency, queryFee, paymentOption, isDefault);
    }
    p.setImageUriTemplate(imageUrl);
    return p;
  }
}
