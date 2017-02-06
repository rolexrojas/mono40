package com.tpago.movil.ui.main.purchase;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.ui.main.list.ListItemHolder;
import com.tpago.movil.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class PurchasePaymentOptionListItemHolderCreator
  implements ListItemHolderCreator<PurchasePaymentOptionListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  PurchasePaymentOptionListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public PurchasePaymentOptionListItemHolder create(@NonNull ViewGroup parent) {
    return new PurchasePaymentOptionListItemHolder(LayoutInflater.from(parent.getContext()).inflate(
      R.layout.commerce_payment_option, parent, false), onClickListener);
  }
}
