package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.product.Product;
import com.gbh.movil.domain.product.Balance;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductItem {
  /**
   * TODO
   */
  protected final Product product;

  /**
   * TODO
   */
  protected Balance balance;

  /**
   * TODO
   *
   * @param product
   *   TODO
   */
  ProductItem(@NonNull Product product) {
    this.product = product;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Product getProduct() {
    return product;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public Balance getBalance() {
    return balance;
  }

  /**
   * TODO
   *
   * @param balance
   *   TODO
   */
  public void setBalance(@Nullable Balance balance) {
    this.balance = balance;
  }
}
