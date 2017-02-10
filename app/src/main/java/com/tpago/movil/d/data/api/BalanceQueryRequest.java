package com.tpago.movil.d.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Bank;

/**
 * TODO
 *
 * @author hecvasro
 */
class BalanceQueryRequest {
  private final String accountType;
  private final String accountAlias;
  private final String accountNumber;
  private final Bank bank;
  private final String currency;
  private final String pin;

  BalanceQueryRequest(@NonNull Product product, @NonNull String pin) {
    this.accountType = product.getType().name();
    this.accountAlias = product.getAlias();
    this.accountNumber = product.getNumber();
    this.bank = product.getBank();
    this.currency = product.getCurrency();
    this.pin = pin;
  }
}
