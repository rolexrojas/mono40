package com.gbh.movil.data.api;

import com.gbh.movil.domain.Bank;

import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

/**
 * TODO
 *
 * @author hecvasro
 */
class AccountHalResource implements HalResource {
  private static final String LINK_QUERY_ACCOUNT_BALANCE = "balance";

  String accountType;
  String accountAlias;
  String accountNumber;
  Bank bank;
  String currency;
  double queryFee;

  @HalLink(name = LINK_QUERY_ACCOUNT_BALANCE)
  String queryBalanceLink;

  @Override
  public String toString() {
    return AccountHalResource.class.getSimpleName() + ":{accountType='" + accountType
      + "',accountAlias='" + accountAlias + "',accountNumber='" + accountNumber + "',bank=" + bank
      + ",currency='" + currency + "',queryFee=" + queryFee + ",queryBalanceLink='"
      + queryBalanceLink + "'}";
  }
}
