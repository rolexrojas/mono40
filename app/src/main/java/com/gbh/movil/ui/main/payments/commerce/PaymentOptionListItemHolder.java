package com.gbh.movil.ui.main.payments.commerce;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class PaymentOptionListItemHolder extends ListItemHolder implements PaymentOptionHolder {
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
  TextView productAliasTextView;
  /**
   * TODO
   */
  @BindView(R.id.product_owner_name)
  TextView userNameTextView;

  PaymentOptionListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }

  @Nullable
  @Override
  public Drawable getRootViewBackground() {
    return getRootView().getBackground();
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
    return productAliasTextView;
  }

  @NonNull
  @Override
  public TextView getUserNameTextView() {
    return userNameTextView;
  }
}
