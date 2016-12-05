package com.gbh.movil.ui.main.payments.purchase;

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
class CommercePaymentOptionListItemHolderCreator
  implements ListItemHolderCreator<CommercePaymentOptionListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  CommercePaymentOptionListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public CommercePaymentOptionListItemHolder create(@NonNull ViewGroup parent) {
    return new CommercePaymentOptionListItemHolder(LayoutInflater.from(parent.getContext()).inflate(
      R.layout.commerce_payment_option, parent, false), onClickListener);
  }
}
