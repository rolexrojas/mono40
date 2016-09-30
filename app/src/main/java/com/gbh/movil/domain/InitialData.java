package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialData {
  /**
   * TODO
   */
  private final Set<Account> accounts;

  /**
   * TODO
   */
  private final List<Transaction> transactions;

  /**
   * TODO
   *
   * @param accounts
   *   TODO
   * @param transactions
   *   TODO
   */
  public InitialData(@NonNull Set<Account> accounts, @NonNull List<Transaction> transactions) {
    this.accounts = accounts;
    this.transactions = transactions;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Set<Account> getAccounts() {
    return accounts;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final List<Transaction> getTransactions() {
    return transactions;
  }
}
