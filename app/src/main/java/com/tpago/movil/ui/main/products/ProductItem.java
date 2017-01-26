package com.tpago.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.misc.Utils;
import com.tpago.movil.domain.Product;
import com.tpago.movil.domain.Balance;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductItem {
  /**
   * TODO
   */
  private final Product product;

  /**
   * TODO
   */
  private Balance balance;

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

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof ProductItem
      && ((ProductItem) object).product.equals(product));
  }

  @Override
  public int hashCode() {
    return product.hashCode();
  }

  @Override
  public String toString() {
    return ProductItem.class.getSimpleName() + ":{product=" + product + ",balance=" + balance + "}";
  }
}
