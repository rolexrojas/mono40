package com.tpago.movil.d.ui.main.purchase;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PurchaseScreen extends Screen {
  /**
   * TODO
   */
  void clearPaymentOptions();

  /**
   * TODO
   *
   * @param product
   *   TODO
   */
  void addPaymentOption(@NonNull Product product);

  /**
   * TODO
   *
   * @param product
   *   TODO
   */
  void markAsSelected(@NonNull Product product);

  /**
   * TODO
   *
   * @param paymentOption
   *   TODO
   */
  void openPaymentScreen(@NonNull Product paymentOption);

  void requestPin();

  void onActivationFinished(boolean succeeded);

  /**
   * TODO
   *
   * @param intent
   *   TODO
   */
  void startActivity(@NonNull Intent intent);

  void showGenericErrorDialog(String message);
  void showGenericErrorDialog();
}
