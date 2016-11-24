package com.gbh.movil.ui.main.payments.transactions.contacts;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Screen;

import java.util.Set;

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
  void setPaymentOptions(@NonNull Set<Product> paymentOptions);
}
