package com.tpago.movil.dep.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.dep.ui.Refreshable;
import com.tpago.movil.dep.ui.Screen;

/**
 * @author hecvasro
 */
interface ProductsScreen extends Screen, Refreshable {
  void clear();

  void add(@NonNull Object item);

  void onBalanceQueried(
    boolean succeeded,
    @NonNull Product product,
    @Nullable Balance balance,
    @Nullable String message);

  void setBalance(@NonNull Product product, @Nullable Balance balance);
}
