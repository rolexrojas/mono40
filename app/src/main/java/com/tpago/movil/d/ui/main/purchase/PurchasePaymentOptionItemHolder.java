package com.tpago.movil.d.ui.main.purchase;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentOptionItemHolder implements PurchasePaymentOptionHolder {
  private final View rootView;

  @BindView(R.id.image_view_background) ImageView backgroundImageView;
  @BindView(R.id.image_view_bank_logo) ImageView bankLogoImageView;
  @BindView(R.id.image_view_issuer) ImageView issuerImageView;
  @BindView(R.id.text_view_owner_name) TextView ownerNameTextView;
  @BindView(R.id.text_view_product_number) TextView productNumberTextView;
  @BindView(R.id.text_view_product_type) TextView productTypeTextView;

  PurchasePaymentOptionItemHolder(@NonNull View rootView) {
    this.rootView = rootView;
    ButterKnife.bind(this, this.rootView);
  }

  @NonNull
  @Override
  public ImageView getBackgroundImageView() {
    return backgroundImageView;
  }

  @NonNull
  @Override
  public ImageView getBankLogoImageView() {
    return bankLogoImageView;
  }

  @NonNull
  @Override
  public ImageView getIssuerImageView() {
    return issuerImageView;
  }

  @NonNull
  @Override
  public TextView getProductNumberTextView() {
    return productNumberTextView;
  }

  @NonNull
  @Override
  public TextView getOwnerNameTextView() {
    return ownerNameTextView;
  }

  @NonNull
  @Override
  public TextView getProductTypeTextView() {
    return productTypeTextView;
  }

  @Nullable
  @Override
  public Drawable getRootViewBackground() {
    return rootView.getBackground();
  }
}
