package com.tpago.movil.dep.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.ui.Refreshable;
import com.tpago.movil.dep.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface ProductsScreen extends Screen, Refreshable {
  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  void add(@NonNull Object item);

  /**
   * TODO
   *
   * @param succeeded
   *   TODO
   * @param product
   *   TODO
   * @param balance
   *   TODO
   */
  void onBalanceQueried(boolean succeeded, @NonNull Product product, @Nullable Balance balance);

  /**
   * TODO
   *
   * @param product
   *   TODO
   * @param balance
   *   TODO
   */
  void setBalance(@NonNull Product product, @Nullable Balance balance);
}
