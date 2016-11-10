package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * Abstract account representation.
 *
 * @author hecvasro
 */
public abstract class Account {
  /**
   * Account's {@link AccountType type}.
   */
  private final AccountType type;
  /**
   * Account's identifier.
   */
  private final String alias;
  /**
   * Account's amount.
   */
  private final String currency;
  /**
   * Account's {@link Bank holder}.
   */
  private final Bank bank;
  /**
   * Cost of querying the balance.
   */
  private double queryFee;

  /**
   * Constructs a new account.
   *
   * @param type
   *   Account's {@link AccountType type}.
   * @param alias
   *   Account's identifier.
   * @param currency
   *   Account's amount.
   * @param bank
   *   Account's {@link Bank holder}.
   * @param queryFee
   *   Cost of querying the balance.
   */
  protected Account(@NonNull AccountType type, @NonNull String alias, @NonNull String currency,
    @NonNull Bank bank, double queryFee) {
    this.type = type;
    this.alias = alias;
    this.currency = currency;
    this.bank = bank;
    this.queryFee = queryFee;
  }

  /**
   * Gets the {@link AccountType type} of the account.
   *
   * @return Account's {@link AccountType type}.
   */
  @NonNull
  public final AccountType getType() {
    return type;
  }

  /**
   * Gets the identifier of the account.
   *
   * @return Account's identifier.
   */
  @NonNull
  public final String getAlias() {
    return alias;
  }

  /**
   * Gets the amount of the account.
   *
   * @return Account's amount.
   */
  @NonNull
  public final String getCurrency() {
    return currency;
  }

  /**
   * Gets the {@link Bank holder} of the account.
   *
   * @return Account's {@link Bank holder}.
   */
  @NonNull
  public final Bank getBank() {
    return bank;
  }

  /**
   * Gets the cost of querying the balance of the account.
   *
   * @return Cost of querying the balance.
   */
  public double getQueryFee() {
    return queryFee;
  }

  /**
   * Sets the cost of querying the balance of the account.
   *
   * @param queryFee
   *   Cost of querying the balance.
   */
  public void setQueryFee(double queryFee) {
    if (queryFee < 0) {
      queryFee = 0;
    }
    this.queryFee = queryFee;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Account &&
      ((Account) object).type.equals(type) && ((Account) object).alias.equals(alias));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(type, alias);
  }

  @Override
  public String toString() {
    return Account.class.getSimpleName() + ":{type=" + type + ",alias='" + alias + "',amount='"
      + currency + "',bank=" + bank + ",queryFee=" + queryFee + "'}";
  }
}
