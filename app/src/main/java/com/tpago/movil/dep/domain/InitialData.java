package com.tpago.movil.dep.domain;

import java.util.List;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitialData {
  private final List<Product> products;
  private final List<Recipient> recipients;

  public InitialData(
    List<Product> products,
    List<Recipient> recipients) {
    this.products = products;
    this.recipients = recipients;
  }

  public final List<Product> getProducts() {
    return products;
  }

  public final List<Recipient> getRecipients() {
    return recipients;
  }
}
