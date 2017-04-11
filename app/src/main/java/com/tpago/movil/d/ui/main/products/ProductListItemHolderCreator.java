package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * @author hecvasro
 */
class ProductListItemHolderCreator implements ListItemHolderCreator<ProductListItemHolder> {
  private final ListItemHolder.OnClickListener onClickListener;
  private final ProductListItemHolder.OnQueryBalanceButtonPressedListener onQueryButtonClickedListener;

  ProductListItemHolderCreator(
    ListItemHolder.OnClickListener onClickListener,
    ProductListItemHolder.OnQueryBalanceButtonPressedListener onQueryButtonClickedListener) {
    this.onClickListener = onClickListener;
    this.onQueryButtonClickedListener = onQueryButtonClickedListener;
  }

  @NonNull
  @Override
  public ProductListItemHolder create(@NonNull ViewGroup parent) {
    return new ProductListItemHolder(
      LayoutInflater
        .from(parent.getContext())
        .inflate(
          R.layout.d_list_item_product,
          parent,
          false),
      onClickListener,
      onQueryButtonClickedListener);
  }
}
