package com.tpago.movil.main.transactions;

import android.content.Context;

import com.tpago.movil.R;
import com.tpago.movil.dep.domain.Product;

/**
 * @author hecvasro
 */
final class SelectedPaymentMethodHolderBinder
  extends BasePaymentMethodHolderBinder<SelectedPaymentMethodHolder> {
  SelectedPaymentMethodHolderBinder(Context context) {
    super(context);
  }

  @Override
  protected String formatIdentifier(Product product) {
    return getString(R.string.from) + " " + super.formatIdentifier(product);
  }
}
