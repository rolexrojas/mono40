package com.tpago.movil.d.ui.main.purchase;

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

  void showGenericErrorDialog(String title, String message);

  void showGenericErrorDialog(String message);

  void showGenericErrorDialog();

  void showUnavailableNetworkError();

  void requestNFCPermissions();

  void setHasCards(boolean hasCards);
}
