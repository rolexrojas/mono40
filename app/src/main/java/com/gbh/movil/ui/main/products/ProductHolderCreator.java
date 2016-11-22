package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.HolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductHolderCreator implements HolderCreator<ProductHolder> {
  private final ProductHolder.OnQueryActionButtonClickedListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ProductHolderCreator(@NonNull ProductHolder.OnQueryActionButtonClickedListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ProductHolder create(@NonNull ViewGroup parent) {
    return new ProductHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_product, parent, false), listener);
  }
}
