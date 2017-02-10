package com.tpago.movil.d.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductListItemHolderCreator implements ListItemHolderCreator<ProductListItemHolder> {
  private final ProductListItemHolder.OnQueryActionButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ProductListItemHolderCreator(@NonNull ProductListItemHolder.OnQueryActionButtonClickedListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ProductListItemHolder create(@NonNull ViewGroup parent) {
    return new ProductListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_product, parent, false), listener);
  }
}
