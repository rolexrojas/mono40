package com.gbh.movil.ui.main.payments.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Screen;

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
}
