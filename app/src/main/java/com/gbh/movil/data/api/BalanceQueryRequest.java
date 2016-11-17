package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
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

  BalanceQueryRequest(@NonNull Account account, @NonNull String pin) {
    this.accountType = account.getType().name();
    this.accountAlias = account.getAlias();
    this.accountNumber = account.getNumber();
    this.bank = account.getBank();
    this.currency = account.getCurrency();
    this.pin = pin;
  }
}
