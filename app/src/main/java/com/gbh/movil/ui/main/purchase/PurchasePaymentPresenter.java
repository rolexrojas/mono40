package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.Presenter;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentPresenter extends Presenter<PurchasePaymentScreen> {
  private final StringHelper stringHelper;

  private final Product paymentOption;

  PurchasePaymentPresenter(@NonNull StringHelper stringHelper, @NonNull Product paymentOption) {
    this.stringHelper = stringHelper;
    this.paymentOption = paymentOption;
  }

  /**
   * TODO
   */
  void start() {
    assertScreen();
    // TODO: Enable NFC detection.
    screen.setPaymentOption(paymentOption);
  }

  /**
   * TODO
   */
  void stop() {
    assertScreen();
    // TODO: Disable NFC detection.
  }
}
