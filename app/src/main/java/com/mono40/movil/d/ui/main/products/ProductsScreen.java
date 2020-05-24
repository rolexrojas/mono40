package com.mono40.movil.d.ui.main.products;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Balance;
import com.mono40.movil.d.ui.Refreshable;
import com.mono40.movil.d.ui.Screen;

/**
 * @author hecvasro
 */
interface ProductsScreen extends Screen, Refreshable {
  void clear();

  void add(Object item);

  void onBalanceQueried(Product product, Pair<Long, Balance> balance);

  void setBalance(@NonNull Product product, Pair<Long, Balance> balance);

  void showGenericErrorDialog(String title, String message);

  void showGenericErrorDialog(String message);

  void showGenericErrorDialog();

  void showUnavailableNetworkError();
}
