package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.ui.Refreshable;
import com.gbh.movil.ui.Screen;

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
   * @param product
   *   TODO
   */
  void add(@NonNull Product product);

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
