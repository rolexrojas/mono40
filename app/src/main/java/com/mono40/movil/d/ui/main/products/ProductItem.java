package com.mono40.movil.d.ui.main.products;

import androidx.core.util.Pair;

import com.google.auto.value.AutoValue;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Balance;
import com.mono40.movil.util.ObjectHelper;

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
    if (ObjectHelper.isNotNull(pair)) {
      queryTime = pair.first;
      balance = pair.second;
    }
  }
}
