package com.tpago.movil.d.ui.main.purchase;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.d.ui.main.list.ListItemHolder;

/**
 * @author hecvasro
 */
class PurchasePaymentOptionListItemHolder
  extends ListItemHolder
  implements PurchasePaymentOptionHolder {
  private final PurchasePaymentOptionItemHolder holder;

  PurchasePaymentOptionListItemHolder(View rootView, OnClickListener onClickListener) {
    super(rootView, onClickListener);
    this.holder = new PurchasePaymentOptionItemHolder(this.rootView);
  }

  @NonNull
  @Override
  public ImageView getBackgroundImageView() {
    return holder.getBackgroundImageView();
  }

  @NonNull
  @Override
  public ImageView getBankLogoImageView() {
    return holder.getBankLogoImageView();
  }

  @NonNull
  @Override
  public ImageView getIssuerImageView() {
    return holder.getIssuerImageView();
  }

  @NonNull
  @Override
  public TextView getProductNumberTextView() {
    return holder.getProductNumberTextView();
  }

  @NonNull
  @Override
  public TextView getOwnerNameTextView() {
    return holder.getOwnerNameTextView();
  }

  @NonNull
  @Override
  public TextView getProductTypeTextView() {
    return holder.getProductTypeTextView();
  }

  @Nullable
  @Override
  public Drawable getRootViewBackground() {
    return holder.getRootViewBackground();
  }
}
