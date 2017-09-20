package com.tpago.movil.d.ui.main.recipient.addition.banks;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class BankListItemHolderCreator implements ListItemHolderCreator<BankListItemHolder> {

  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener
   *   TODO
   */
  BankListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public BankListItemHolder create(@NonNull ViewGroup parent) {
    return new BankListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.d_list_item_bank, parent, false), onClickListener);
  }
}
