package com.gbh.movil.ui.main.payments.commerce;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.ui.main.list.ListItemHolder;

/**
 * TODO
 *
 * @author hecvasro
 */
class CommercePaymentOptionListItemHolder extends ListItemHolder implements CommercePaymentOptionHolder {
  private final CommercePaymentOptionItemHolder holder;

  CommercePaymentOptionListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
    this.holder = new CommercePaymentOptionItemHolder(this.rootView);
  }

  @Nullable
  @Override
  public Drawable getRootViewBackground() {
    return holder.getRootViewBackground();
  }

  @NonNull
  @Override
  public ImageView getBankLogoImageView() {
    return holder.bankLogoImageView;
  }

  @NonNull
  @Override
  public TextView getProductIdentifierTextView() {
    return holder.getProductIdentifierTextView();
  }

  @NonNull
  @Override
  public TextView getProductNumberTextView() {
    return holder.getProductNumberTextView();
  }

  @NonNull
  @Override
  public TextView getProductOwnerNameTextView() {
    return holder.getProductOwnerNameTextView();
  }
}
