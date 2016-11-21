package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolder;
import com.gbh.movil.ui.main.list.ItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionItemHolderCreator implements ItemHolderCreator<ActionItemHolder> {
  private final ItemHolder.OnClickListener listener;

  /**
   * TODO
   *
   * @param listener
   *   TODO
   */
  ActionItemHolderCreator(@NonNull ItemHolder.OnClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ActionItemHolder create(@NonNull ViewGroup parent) {
    return new ActionItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_action, parent, false), listener);
  }
}
