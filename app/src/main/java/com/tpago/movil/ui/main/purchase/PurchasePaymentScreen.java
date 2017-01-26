package com.tpago.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Product;
import com.tpago.movil.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PurchasePaymentScreen extends Screen {
  /**
   * TODO
   *
   * @param message
   *   TODO
   */
  void setMessage(@NonNull String message);

  /**
   * TODO
   *
   * @param paymentOption
   *   TODO
   */
  void setPaymentOption(@NonNull Product paymentOption);

  /**
   * TODO
   */
  void animateAndTerminate();
}
