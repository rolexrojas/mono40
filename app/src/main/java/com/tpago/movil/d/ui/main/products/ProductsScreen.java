package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.ui.Refreshable;
import com.tpago.movil.d.ui.Screen;

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
