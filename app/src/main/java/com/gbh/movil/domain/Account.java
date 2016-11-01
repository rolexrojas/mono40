package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Abstract account representation.
 *
 * @author hecvasro
 */
public abstract class Account {
  /**
   * Account's {@link AccountType type}.
   */
  @AccountType
  private final int type;
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
   * Description of the cost of querying the balance.
   */
  private String queryFeeDescription;
  /**
   * Url for querying the balance.
   */
  private String queryBalanceUrl;

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
   * @param queryFeeDescription
   *   Description of the cost of querying the balance.
   * @param queryBalanceUrl
   *   Url for querying the balance.
   */
  public Account(@AccountType int type, @NonNull String alias, @NonNull String currency,
    @NonNull Bank bank, double queryFee, @NonNull String queryFeeDescription,
    @NonNull String queryBalanceUrl) {
    this.type = type;
    this.alias = alias;
    this.currency = currency;
    this.bank = bank;
    this.queryFee = queryFee;
    this.queryFeeDescription = queryFeeDescription;
    this.queryBalanceUrl = queryBalanceUrl;
  }

  /**
   * Gets the {@link AccountType type} of the account.
   *
   * @return Account's {@link AccountType type}.
   */
  @AccountType
  public final int getType() {
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

  /**
   * Gets the description of the cost of querying the balance of the account.
   *
   * @return Description of the cost of querying the balance.
   */
  @NonNull
  public String getQueryFeeDescription() {
    return queryFeeDescription;
  }

  /**
   * Sets the description of the cost of querying the balance of the account.
   *
   * @param queryFeeDescription
   *   Description of the cost of querying the balance.
   */
  public void setQueryFeeDescription(@NonNull String queryFeeDescription) {
    this.queryFeeDescription = queryFeeDescription;
  }

  /**
   * Gets the url for querying the balance of the account.
   *
   * @return Url for querying the balance.
   */
  @NonNull
  public String getQueryBalanceUrl() {
    return queryBalanceUrl;
  }

  /**
   * Sets the url for querying the balance of the account.
   *
   * @param queryBalanceUrl
   *   Url for querying the balance.
   */
  public void setQueryBalanceUrl(@NonNull String queryBalanceUrl) {
    this.queryBalanceUrl = queryBalanceUrl;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (object != null && object instanceof Account &&
      ((Account) object).type == type && ((Account) object).alias.equals(alias));
  }

  @Override
  public int hashCode() {
    return alias.hashCode();
  }

  @Override
  public String toString() {
    return "Account:{type=" + type + ",alias='" + alias + "',amount='" + currency + "',bank="
      + bank.toString() + ",queryFee=" + queryFee + ",queryFeeDescription='" + queryFeeDescription
      + "',queryBalanceUrl='" + queryBalanceUrl + "'}";
  }
}
