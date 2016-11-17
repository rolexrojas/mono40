package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.product.Product;
import com.gbh.movil.domain.Bank;

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
    this.accountType = product.getIdentifier().name();
    this.accountAlias = product.getAlias();
    this.accountNumber = product.getNumber();
    this.bank = product.getBank();
    this.currency = product.getCurrency();
    this.pin = pin;
  }
}
