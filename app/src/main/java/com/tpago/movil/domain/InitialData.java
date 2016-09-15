package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author hecvasro
 */
public final class InitialData {
  private final List<BankAccount> bankAccounts;
  private final List<CreditCard> creditCards;

  public InitialData(@NonNull List<BankAccount> bankAccounts,
    @NonNull List<CreditCard> creditCards) {
    this.bankAccounts = bankAccounts;
    this.creditCards = creditCards;
  }

  @NonNull
  public final List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  @NonNull
  public final List<CreditCard> getCreditCards() {
    return creditCards;
  }
}
