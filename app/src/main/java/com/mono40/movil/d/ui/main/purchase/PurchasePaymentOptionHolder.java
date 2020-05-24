package com.mono40.movil.d.ui.main.purchase;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.d.data.util.Holder;

/**
 * @author hecvasro
 */
interface PurchasePaymentOptionHolder extends Holder {
  @NonNull ImageView getBackgroundImageView();
  @NonNull ImageView getBankLogoImageView();
  @NonNull ImageView getIssuerImageView();
  @NonNull TextView getProductNumberTextView();
  @NonNull TextView getOwnerNameTextView();
  @NonNull TextView getProductTypeTextView();
  @Nullable Drawable getRootViewBackground();
}
