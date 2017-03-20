package com.tpago.movil.dep.ui.main.purchase;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.dep.data.util.Holder;

/**
 * @author hecvasro
 */
interface PurchasePaymentOptionHolder extends Holder {
  @NonNull ImageView getBackgroundImageView();
  @NonNull ImageView getBankLogoImageView();
  @NonNull ImageView getIssuerImageView();
  @NonNull TextView getBankNameTextView();
  @NonNull TextView getProductNumberTextView();
  @NonNull TextView getOwnerNameTextView();
  @NonNull TextView getProductTypeTextView();
  @Nullable Drawable getRootViewBackground();
}
