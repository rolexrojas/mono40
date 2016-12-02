package com.gbh.movil.ui.main.payments.transactions.contacts;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Screen;

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
