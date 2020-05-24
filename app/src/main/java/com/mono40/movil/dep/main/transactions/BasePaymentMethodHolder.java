package com.mono40.movil.dep.main.transactions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodHolder {
  @BindView(R.id.image_view_bank_logo) ImageView bankLogoImageView;
  @BindView(R.id.text_view_product_identifier) TextView productIdentifierTextView;

  BasePaymentMethodHolder(View view) {
    ButterKnife.bind(this, view);
  }

  final ImageView getBankLogoImageView() {
    return bankLogoImageView;
  }

  final TextView getProductIdentifierTextView() {
    return productIdentifierTextView;
  }
}
