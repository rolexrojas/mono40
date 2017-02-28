package com.tpago.movil.dep.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.Screen;

import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PhoneNumberTransactionCreationScreen extends Screen {
  /**
   * TODO
   *
   * @param paymentOptions
   *   TODO
   */
  void setPaymentOptions(@NonNull List<Product> paymentOptions);

  /**
   * TODO
   *
   * @param currency
   *   TODO
   */
  void setPaymentOptionCurrency(@NonNull String currency);

  /**
   * TODO
   */
  void clearAmount();

  /**
   * TODO
   *
   * @param succeeded
   *   TODO
   */
  void setPaymentResult(boolean succeeded);
}
