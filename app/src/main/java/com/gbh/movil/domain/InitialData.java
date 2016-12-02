package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialData {
  private final List<Product> products;
  private final List<Recipient> recipients;

  public InitialData(@NonNull List<Product> products, @NonNull List<Recipient> recipients) {
    this.products = products;
    this.recipients = recipients;
  }

  public InitialData(@NonNull List<Product> products) {
    this(products, new ArrayList<Recipient>());
  }

  @NonNull
  public final List<Product> getProducts() {
    return products;
  }

  @NonNull
  public final List<Recipient> getRecipients() {
    return recipients;
  }
}
