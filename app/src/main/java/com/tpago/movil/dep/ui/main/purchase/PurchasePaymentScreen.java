package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.Screen;

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
