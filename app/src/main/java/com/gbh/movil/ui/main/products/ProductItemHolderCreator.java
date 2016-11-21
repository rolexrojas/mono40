package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductItemHolderCreator implements ItemHolderCreator<ProductItemHolder> {
  private final ProductItemHolder.OnQueryActionButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ProductItemHolderCreator(@NonNull ProductItemHolder.OnQueryActionButtonClickedListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ProductItemHolder create(@NonNull ViewGroup parent) {
    return new ProductItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_product, parent, false), listener);
  }
}
