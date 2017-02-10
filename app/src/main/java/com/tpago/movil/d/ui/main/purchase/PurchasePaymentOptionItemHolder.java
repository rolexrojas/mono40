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
  /**
   * TODO
   */
  private final View rootView;

  /**
   * TODO
   */
  @BindView(R.id.bank_logo)
  ImageView bankLogoImageView;
  /**
   * TODO
   */
  @BindView(R.id.product_identifier)
  TextView productIdentifierTextView;
  /**
   * TODO
   */
  @BindView(R.id.product_number)
  TextView productNumberTextView;
  /**
   * TODO
   */
  @BindView(R.id.product_owner_name)
  TextView productOwnerNameTextView;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   */
  PurchasePaymentOptionItemHolder(@NonNull View rootView) {
    this.rootView = rootView;
    ButterKnife.bind(this, this.rootView);
  }

  @Nullable
  @Override
  public Drawable getRootViewBackground() {
    return rootView.getBackground();
  }

  @NonNull
  @Override
  public ImageView getBankLogoImageView() {
    return bankLogoImageView;
  }

  @NonNull
  @Override
  public TextView getProductIdentifierTextView() {
    return productIdentifierTextView;
  }

  @NonNull
  @Override
  public TextView getProductNumberTextView() {
    return productNumberTextView;
  }

  @NonNull
  @Override
  public TextView getProductOwnerNameTextView() {
    return productOwnerNameTextView;
  }
}
