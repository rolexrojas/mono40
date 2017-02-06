package com.tpago.movil.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Product;
import com.tpago.movil.ui.Screen;

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
