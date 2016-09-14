package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * @author hecvasro
 */
public abstract class Account {
  /**
   * TODO
   */
  @AccountType
  private final int type;

  /**
   * TODO
   */
  private final String alias;

  /**
   * TODO
   */
  private final String currency;

  /**
   * TODO
   */
  private final Bank bank;

  /**
   * TODO
   */
  private double queryFee;

  /**
   * TODO
   */
  private String queryFeeDescription;

  /**
   * TODO
   */
  private String balanceUrl;

  /**
   * TODO
   *
   * @param type
   *   TODO
   * @param alias
   *   TODO
   * @param currency
   *   TODO
   * @param bank
   *   TODO
   * @param queryFee
   *   TODO
   * @param queryFeeDescription
   *   TODO
   * @param balanceUrl
   *   TODO
   */
  public Account(@AccountType int type, @NonNull String alias, @NonNull String currency,
    @NonNull Bank bank, double queryFee, @NonNull String queryFeeDescription,
    @NonNull String balanceUrl) {
    this.type = type;
    this.alias = alias;
    this.currency = currency;
    this.bank = bank;
    this.queryFee = queryFee;
    this.queryFeeDescription = queryFeeDescription;
    this.balanceUrl = balanceUrl;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @AccountType
  public final int getType() {
    return type;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getAlias() {
    return alias;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getCurrency() {
    return currency;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Bank getBank() {
    return bank;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public double getQueryFee() {
    return queryFee;
  }

  /**
   * TODO
   *
   * @param queryFee
   *   TODO
   */
  public void setQueryFee(double queryFee) {
    this.queryFee = queryFee;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public String getQueryFeeDescription() {
    return queryFeeDescription;
  }

  /**
   * TODO
   *
   * @param queryFeeDescription
   *   TODO
   */
  public void setQueryFeeDescription(@NonNull String queryFeeDescription) {
    this.queryFeeDescription = queryFeeDescription;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public String getBalanceUrl() {
    return balanceUrl;
  }

  /**
   * TODO
   *
   * @param balanceUrl
   *   TODO
   */
  public void setBalanceUrl(@NonNull String balanceUrl) {
    this.balanceUrl = balanceUrl;
  }

  @Override
  public boolean equals(Object object) {
    // TODO
    return super.equals(object);
  }

  @Override
  public int hashCode() {
    // TODO
    return super.hashCode();
  }

  @Override
  public String toString() {
    // TODO
    return super.toString();
  }
}
