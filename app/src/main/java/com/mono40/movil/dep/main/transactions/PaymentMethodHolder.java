package com.mono40.movil.dep.main.transactions;

import android.view.View;
import android.widget.TextView;

import com.mono40.movil.R;

import butterknife.BindView;

/**
 * @author hecvasro
 */
final class PaymentMethodHolder extends BasePaymentMethodHolder {
  @BindView(R.id.text_view_product_number) TextView productNumberTextView;

  PaymentMethodHolder(View view) {
    super(view);
  }

  final TextView getProductNumberTextView() {
    return productNumberTextView;
  }
}
