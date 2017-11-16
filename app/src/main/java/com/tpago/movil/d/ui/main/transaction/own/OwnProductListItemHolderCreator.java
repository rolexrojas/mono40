package com.tpago.movil.d.ui.main.transaction.own;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * @author hecvasro
 */
class OwnProductListItemHolderCreator implements ListItemHolderCreator<OwnProductListItemHolder> {
  private final OwnProductListItemHolder.OnButtonClickedListener onButtonClickedListener;

  OwnProductListItemHolderCreator(OwnProductListItemHolder.OnButtonClickedListener onButtonClickedListener) {
    this.onButtonClickedListener = onButtonClickedListener;
  }

  @NonNull
  @Override
  public OwnProductListItemHolder create(@NonNull ViewGroup parent) {
    return new OwnProductListItemHolder(
      LayoutInflater
        .from(parent.getContext())
        .inflate(
          R.layout.d_list_item_own_product,
          parent,
          false),
      onButtonClickedListener);
  }
}
