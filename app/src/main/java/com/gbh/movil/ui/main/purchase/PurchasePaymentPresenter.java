package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.ui.Presenter;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentPresenter extends Presenter<PurchasePaymentScreen> {
  private final StringHelper stringHelper;

  PurchasePaymentPresenter(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }
}
