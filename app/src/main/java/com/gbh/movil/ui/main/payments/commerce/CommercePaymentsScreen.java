package com.gbh.movil.ui.main.payments.commerce;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface CommercePaymentsScreen extends Screen {
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
}
