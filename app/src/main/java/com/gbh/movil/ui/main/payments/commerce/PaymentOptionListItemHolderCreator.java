package com.gbh.movil.ui.main.payments.commerce;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;
import com.gbh.movil.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class PaymentOptionListItemHolderCreator
  implements ListItemHolderCreator<PaymentOptionListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  PaymentOptionListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public PaymentOptionListItemHolder create(@NonNull ViewGroup parent) {
    return new PaymentOptionListItemHolder(LayoutInflater.from(parent.getContext()).inflate(
      R.layout.list_item_commerce_payment_option, parent, false), onClickListener);
  }
}
