package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialData {
  /**
   * TODO
   */
  private final List<BankAccount> bankAccounts;

  /**
   * TODO
   */
  private final List<CreditCard> creditCards;

  /**
   * TODO
   *
   * @param bankAccounts
   *   TODO
   * @param creditCards
   *   TODO
   */
  public InitialData(@NonNull List<BankAccount> bankAccounts,
    @NonNull List<CreditCard> creditCards) {
    this.bankAccounts = bankAccounts;
    this.creditCards = creditCards;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final List<CreditCard> getCreditCards() {
    return creditCards;
  }
}
