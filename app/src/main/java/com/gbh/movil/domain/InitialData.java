package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.product.Product;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialData {
  private final Set<Product> products;
  private final Set<Recipient> recipients;

  public InitialData(@NonNull Set<Product> products, @NonNull Set<Recipient> recipients) {
    this.products = products;
    this.recipients = recipients;
  }

  public InitialData(@NonNull Set<Product> products) {
    this(products, new HashSet<Recipient>());
  }

  @NonNull
  public final Set<Product> getProducts() {
    return products;
  }

  @NonNull
  public final Set<Recipient> getRecipients() {
    return recipients;
  }
}
