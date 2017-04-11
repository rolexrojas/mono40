package com.tpago.movil.d.ui.main.products;

import android.support.v4.util.Pair;

import com.google.auto.value.AutoValue;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Balance;

import static com.tpago.movil.util.Objects.checkIfNotNull;

/**
 * @author hecvasro
 */
@AutoValue
abstract class ProductItem {
  static ProductItem create(Product product) {
    return new AutoValue_ProductItem(product);
  }

  private long queryTime;
  private Balance balance;

  protected ProductItem() {
  }

  abstract Product getProduct();

  final long getQueryTime() {
    return queryTime;
  }

  final Balance getBalance() {
    return balance;
  }

  final void setBalance(Pair<Long, Balance> pair) {
    if (checkIfNotNull(pair)) {
      queryTime = pair.first;
      balance = pair.second;
    }
  }
}
