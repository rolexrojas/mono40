package com.gbh.movil.ui.main.payments.commerce;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.data.util.Holder;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PaymentOptionHolder extends Holder {
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
  TextView getUserNameTextView();
}
