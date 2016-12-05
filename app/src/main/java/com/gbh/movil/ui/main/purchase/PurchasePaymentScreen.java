package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Screen;

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
}
