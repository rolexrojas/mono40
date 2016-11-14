package com.gbh.movil.data.api;

import android.text.TextUtils;

import java.util.List;

import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

/**
 * TODO
 *
 * @author hecvasro
 */
class QueryHalResource implements HalResource {
  private static final String LINK_ACCOUNTS = "accounts";
  private static final String LINK_CREDIT_CARDS = "creditCards";

  List<AccountHalResource> accounts;
  List<AccountHalResource> creditCards;

  @HalLink(name = LINK_ACCOUNTS)
  String accountsLink;
  @HalLink(name = LINK_CREDIT_CARDS)
  String creditCardsLink;

  @Override
  public String toString() {
    return QueryHalResource.class.getSimpleName() + ":{accounts=[" + TextUtils.join(",", accounts)
      + "],creditCards=[" + TextUtils.join(",", creditCards) + "],accountsLink='" + accountsLink
      + "',creditCardsLink='" + creditCardsLink + "'}";
  }
}
