package com.tpago.movil.dep.ui.main.purchase;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.dep.data.util.Holder;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PurchasePaymentOptionHolder extends Holder {
  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  Drawable getRootViewBackground();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  ImageView getBankLogoImageView();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  TextView getProductIdentifierTextView();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  TextView getProductNumberTextView();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  TextView getProductOwnerNameTextView();
}
