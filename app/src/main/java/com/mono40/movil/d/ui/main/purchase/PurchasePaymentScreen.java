package com.mono40.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;

import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.ui.Screen;

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
